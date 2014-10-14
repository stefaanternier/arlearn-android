package org.celstec.arlearn2.android.delegators;

import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import daoBase.DaoConfiguration;
import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.QueryBuilder;
import org.celstec.arlearn2.android.db.Constants;
import org.celstec.arlearn2.android.events.ResponseEvent;
import org.celstec.arlearn2.android.util.AppengineFileUploader;
import org.celstec.arlearn2.android.util.FileDownloader;
import org.celstec.arlearn2.beans.generalItem.MultipleChoiceAnswerItem;
import org.celstec.arlearn2.beans.run.ResponseList;
import org.celstec.arlearn2.client.ResponseClient;
import org.celstec.dao.gen.GeneralItemLocalObject;
import org.celstec.dao.gen.GeneralItemMediaLocalObject;
import org.celstec.dao.gen.ResponseLocalObject;
import org.celstec.dao.gen.ResponseLocalObjectDao;
import org.celstec.arlearn2.beans.run.Response;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.HashMap;

/**
 * ****************************************************************************
 * Copyright (C) 2013 Open Universiteit Nederland
 * <p/>
 * This library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 * <p/>
 * Contributors: Stefaan Ternier
 * ****************************************************************************
 */
public class ResponseDelegator extends AbstractDelegator{

    private static ResponseDelegator instance;
    private static HashMap<Long, Long> syncDates = new HashMap<Long, Long>();

    private ResponseDelegator() {
        ARL.eventBus.register(this);
    }

    public void createMultipleChoiceResponse(GeneralItemLocalObject generalItemLocalObject, long runId,  MultipleChoiceAnswerItem answerItem) {
        try {
            JSONObject responseValueJson = new JSONObject();
            responseValueJson.put("isCorrect", answerItem.getIsCorrect());
            responseValueJson.put("answer", answerItem.getAnswer());
            createResponse(generalItemLocalObject,  runId,responseValueJson.toString());
        } catch (JSONException e) {
            Log.e("exception", e.getMessage(), e);
        }

    }

    public void createResponse(GeneralItemLocalObject generalItemLocalObject, long runId, String responseValue) {
        ResponseLocalObject response = new ResponseLocalObject();
        response.setTimeStamp(ARL.time.getServerTime());
        response.setAccountLocalObject(ARL.accounts.getLoggedInAccount());
        response.setIsSynchronized(false);
        response.setNextSynchronisationTime(0l);
        response.setValue(responseValue);
        response.setRunId(runId);
        response.setGeneralItem(generalItemLocalObject.getId());
        setLocationDetails(response);
        DaoConfiguration.getInstance().getResponseLocalObjectDao().insertOrReplace(response);
        DaoConfiguration.getInstance().getRunLocalObjectDao().load(response.getRunId()).resetResponses();
    }

    public static ResponseDelegator getInstance() {
        if (instance == null) {
            instance = new ResponseDelegator();
        }
        return instance;
    }

    private void setLocationDetails(ResponseLocalObject response) {
        LocationManager locationManager = (LocationManager) ARL.ctx.getSystemService(ARL.ctx.LOCATION_SERVICE);

        String locationProviderNetwork = LocationManager.NETWORK_PROVIDER;
        String locationProviderGPS = LocationManager.GPS_PROVIDER;
        Location lastKnownLocation = locationManager.getLastKnownLocation(locationProviderGPS);
        if (lastKnownLocation == null) {
            lastKnownLocation =locationManager.getLastKnownLocation(locationProviderNetwork);
        }
        if (lastKnownLocation != null) {
            response.setLat(lastKnownLocation.getLatitude());
            response.setLng(lastKnownLocation.getLongitude());
        }
    }

    public void syncResponses() {
        ARL.eventBus.post(new SyncResponses());
    }

    public void syncResponses(long runId) {
        ARL.eventBus.post(new SyncResponses(runId));
    }

    private void onEventAsync(SyncResponses syncResponses) {
        ResponseLocalObjectDao dao = DaoConfiguration.getInstance().getResponseLocalObjectDao();
        long serverTime = ARL.time.getServerTime();
        Log.i(SYNC_TAG, "Syncing responses");

        QueryBuilder<ResponseLocalObject> qb = dao.queryBuilder().orderAsc(ResponseLocalObjectDao.Properties.TimeStamp);
        Query<ResponseLocalObject> notSyncedQuery = qb.where(ResponseLocalObjectDao.Properties.NextSynchronisationTime.lt(serverTime), ResponseLocalObjectDao.Properties.IsSynchronized.eq(false)).build();

        notSyncedQuery.setParameter(0, serverTime);

        for (ResponseLocalObject response : notSyncedQuery.list()) { //uploading responses
            synchronize(response);
        }

        if (syncResponses.getRunId() != null) { //downloading responses
            synchronizeResponsesWithServer(syncResponses.getRunId());
            synchronizeResponseFilesWithServer(syncResponses.getRunId());
        }
    }



    private void synchronizeWithoutFile(ResponseLocalObject response) {
        String token = returnTokenIfOnline();
        if (token != null) {
            Response responseBean = response.getBean();
            ResponseEvent re = new ResponseEvent(response.getRunId());
            if (response.getRevoked()) {
                Response responseResult =  ResponseClient.getResponseClient().deleteResponse(token, response.getId());
                re.setDeletion(true);
                if (responseResult.getRevoked() == null) {
                    System.out.println("test");
                }
                if (responseResult.getRevoked()){
                    response.setIsSynchronized(true);
                }
            } else {
                Response responseResult = ResponseClient.getResponseClient().publishAction(token, responseBean);
                DaoConfiguration.getInstance().getResponseLocalObjectDao().delete(response);
                response.setId(responseResult.getResponseId());
                response.setIsSynchronized(true);
            }
            DaoConfiguration.getInstance().getResponseLocalObjectDao().insertOrReplace(response);

            ARL.eventBus.post(re);
            response.getGeneralItemLocalObject().resetResponses();
        }
    }
    private void synchronize(ResponseLocalObject response) {
        if (!response.hasFile() || response.getRevoked()) {
            synchronizeWithoutFile(response);
            return;
        }
        Response responseBean = response.getBean();

        AppengineFileUploader uploader = new AppengineFileUploader(
                    response.getRunId(),
                    response.getAccountLocalObject().getFullId(),
                    response.getUri().getLastPathSegment());


        String token = returnTokenIfOnline();
        if (token != null) {
            String uploadUrl = uploader.requestUploadUrl(token) ;
            if (uploadUrl == null) {
                Integer amountOfAttempts = response.getAmountOfSynchronisationAttempts();
                if (amountOfAttempts == null) amountOfAttempts = 0;
                response.setAmountOfSynchronisationAttempts(amountOfAttempts + 1);
                response.setNextSynchronisationTime(ARL.time.getServerTime() + (amountOfAttempts * 5000));
                DaoConfiguration.getInstance().getResponseLocalObjectDao().insertOrReplace(response);
            } else {
                Uri uri = response.getUri();
                InputStream is = null;
                try {
                    is = ARL.getContext().getContentResolver().openInputStream(uri);
                    if (uploader.publishData(uploadUrl, is, response.getContentType(), uri.getLastPathSegment())) {
                        response.setIsSynchronized(true);
                    } else {
                        int amountOfAttempts = response.getAmountOfSynchronisationAttempts();
                        response.setAmountOfSynchronisationAttempts(amountOfAttempts + 1);
                        response.setNextSynchronisationTime(ARL.time.getServerTime() + (amountOfAttempts * 5000));
                    }
                    DaoConfiguration.getInstance().getResponseLocalObjectDao().insertOrReplace(response);
                    Response responseResult = ResponseClient.getResponseClient().publishAction(token, responseBean);
                    DaoConfiguration.getInstance().getResponseLocalObjectDao().delete(response);
                    response.setId(responseResult.getResponseId());
                    response.setIsSynchronized(true);
                    response.setThumbnailUriAsString(response.getUriAsString());
                    DaoConfiguration.getInstance().getResponseLocalObjectDao().insertOrReplace(response);
                    ARL.eventBus.post(new ResponseEvent(response.getRunId()));
                    response.getGeneralItemLocalObject().resetResponses();

                } catch (FileNotFoundException e) {
                    Log.e("ARLearn", e.getMessage(), e);
                }


            }
        }

//        ResponseClient.getResponseClient().publishAction(token, response.getBean())
    }

    private void synchronizeResponsesWithServer(long runId) {
        String token = returnTokenIfOnline();
        if (token != null) {
            ResponseList rl = ResponseClient.getResponseClient().getResponses(token, runId, getLastSyncDate(runId));
            ResponseEvent responseEvent = null;
            for (Response response : rl.getResponses()) {
                ResponseLocalObject responseLocalObject = DaoConfiguration.getInstance().getResponseLocalObjectDao().loadDeep(response.getResponseId());
                if (responseLocalObject == null) {
                    responseLocalObject = new ResponseLocalObject(response);
                    DaoConfiguration.getInstance().getResponseLocalObjectDao().insertOrReplace(responseLocalObject);
                    if (responseEvent == null) {
                        responseEvent = new ResponseEvent(runId);
                        if (responseLocalObject.getGeneralItemLocalObject() != null) responseLocalObject.getGeneralItemLocalObject().resetResponses();
                    }

                } else {
                    if (response.getTimestamp() > responseLocalObject.getTimeStamp()) {
                        responseLocalObject.setValuesFromBean(response);
                        DaoConfiguration.getInstance().getResponseLocalObjectDao().insertOrReplace(responseLocalObject);
                    }
                }

            }
            if (responseEvent != null) {
                DaoConfiguration.getInstance().getRunLocalObjectDao().load(runId).resetResponses();
                ARL.eventBus.post(responseEvent);
            }
        }
    }

    private void synchronizeResponseFilesWithServer(Long runId) {
        ResponseLocalObjectDao dao = DaoConfiguration.getInstance().getResponseLocalObjectDao();
        QueryBuilder<ResponseLocalObject> qb = dao.queryBuilder().orderAsc(ResponseLocalObjectDao.Properties.TimeStamp);
        Query<ResponseLocalObject> notSyncedQuery = qb.where(ResponseLocalObjectDao.Properties.UriAsString.like("http://%"), ResponseLocalObjectDao.Properties.RunId.eq(runId)).build();
        for (ResponseLocalObject response : notSyncedQuery.list()) {
            File targetFile = urlToCacheFile(runId, response.getId(), response.getUri().getLastPathSegment());
            Uri newUri = Uri.fromFile(targetFile);
            try {
                FileDownloader fd = new FileDownloader(response.getUriAsString(), targetFile);
                fd.download();
                boolean insert = false;
                if (fd.getTargetLocation().exists()) {
                    response.setUriAsString(newUri.toString());
                    insert = true;
                }
                if (response.getThumbnailUriAsString() != null && !"".equals(response.getThumbnailUriAsString())) {
                    targetFile = urlToCacheFile(runId, response.getId(), "thumb_"+response.getThumbnailUri().getLastPathSegment());
                    newUri = Uri.fromFile(targetFile);
                    fd = new FileDownloader(response.getThumbnailUriAsString(), targetFile);
                    fd.download();
                    if (fd.getTargetLocation().exists()) {
                        response.setThumbnailUriAsString(newUri.toString());
                    }
                }
                if (insert) DaoConfiguration.getInstance().getResponseLocalObjectDao().insertOrReplace(response);
            } catch (MalformedURLException e) {
                Log.e("ARLearn", e.getMessage(), e);
            } catch (FileNotFoundException e) {
                Log.e("ARLearn", e.getMessage(), e);
            }
        }
    }

    private long getLastSyncDate(long runId) {
        if (syncDates.containsKey(runId)) {
            return syncDates.get(runId);
        }
        return 0l;
    }

    private class SyncResponses {
        private Long runId ;

        private SyncResponses(Long runId) {
            this.runId = runId;
        }

        private SyncResponses() {
        }

        public Long getRunId() {
            return runId;
        }

        public void setRunId(Long runId) {
            this.runId = runId;
        }
    }

    private File urlToCacheFile(long runId, long responseId, String fileName) {
        return new File(getCacheDir2(runId), responseId+"-"+fileName);

    }
    private File getCacheDir2(long runId) {
        File sdcard = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
        File cacheDirFile = new File(sdcard, Constants.CACHE_DIR);
        if (!cacheDirFile.exists())
            cacheDirFile.mkdir();
        File incommingDir = new File(cacheDirFile, Constants.INCOMMING+"_runs");
        if (!incommingDir.exists())
            incommingDir.mkdir();

        File runDir = new File(incommingDir, "" + runId);
        if (!runDir.exists())
            runDir.mkdir();

        return runDir;
    }
}
