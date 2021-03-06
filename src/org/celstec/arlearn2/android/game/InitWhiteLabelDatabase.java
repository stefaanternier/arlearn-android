package org.celstec.arlearn2.android.game;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.view.View;
import daoBase.DaoConfiguration;
import de.greenrobot.dao.internal.DaoConfig;
import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.delegators.ARL;
import org.celstec.arlearn2.android.delegators.AccountDelegator;
import org.celstec.arlearn2.android.delegators.GameDelegator;
import org.celstec.arlearn2.android.delegators.game.GameDownloadManager;
import org.celstec.arlearn2.android.delegators.game.GameDownloadManager2;
import org.celstec.arlearn2.android.views.DownloadViewManager;
import org.celstec.dao.gen.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
public class InitWhiteLabelDatabase {

    private Long gameId;
    private Long runId;
    protected List<Long> gameIdsLong;

    protected Context context;

    public static InitWhiteLabelDatabase getWhiteLabelDatabaseIniter(Context ctx) {
        if (ARL.config.getBooleanProperty("white_label_online_sync")) {
            return new InitWhiteLabelDatabaseOnlineSync(ctx);
        }
        return new InitWhiteLabelDatabase(ctx);
    }

    public InitWhiteLabelDatabase(Context context) {
        this.context = context;
    }

    public static List<Long> getGameIds(){
        String gameIds = ARL.config.getProperty("white_label_gameId");
        List<String> list = new ArrayList<String>(Arrays.asList(gameIds.split(",")));
        List<Long> gameIdsLong= new ArrayList<Long>(list.size());
        for (String gameId: list){
            gameIdsLong.add(Long.parseLong(gameId));
        }
        return gameIdsLong;
    }

    public void init() {
        ARL.eventBus.register(this);
        ARL.eventBus.post(new AsyncStartInitDB());
    }

    public void onEventAsync(AsyncStartInitDB event) {
        try {
            String gameIds = ARL.config.getProperty("white_label_gameId");
            List<String> list = new ArrayList<String>(Arrays.asList(gameIds.split(",")));
            gameIdsLong = new ArrayList<Long>(list.size());
            for (String gameId : list) {
                gameIdsLong.add(Long.parseLong(gameId));
            }
            if (ARL.config.getProperty("white_label_runId") != null) {
                runId = Long.parseLong(ARL.config.getProperty("white_label_runId"));
            } else {
                runId = 0l;
            }
            if (ARL.config.getBooleanProperty("white_label_online")) {

            } else {
                loadGameScript();
                loadGameFiles();
                loadRunScript();
                createDefaultAccount();
                createMaps();
                ARL.eventBus.post(new SyncReady(true));
            }

        } catch (Exception e) {
            e.printStackTrace();
            ARL.eventBus.post(new SyncReady(false));
        }

        ARL.eventBus.unregister(this);
    }

    private void createMaps() {
        if (ARL.config.getProperty("white_label_map_file")!= null) {
            AssetManager assetManager = context.getAssets();
            String filename = ARL.config.getProperty("white_label_map_file");
            try {
                InputStream in = assetManager.open(filename);
                File outFile = new File("/mnt/sdcard/osmdroid/", filename);
                OutputStream out = new FileOutputStream(outFile);
                copyFile(in, out);
                in.close();
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }

    protected void loadGameScript() {
        if (gameId != null) {
            GameDelegator.getInstance().loadGameFromFile(context, gameId);
        } else {
            for (Long gameIdLong:gameIdsLong) {
                GameDelegator.getInstance().loadGameFromFile(context, gameIdLong);
            }
        }
    }

    protected void loadGameFiles(){
        System.out.println("load game files in superclass");
            for (long gameId: gameIdsLong) {
                GameDelegator.getInstance().retrieveGameFilesFromFile(context, gameId);
            }
    }

    private void loadRunScript() {
        for (GameLocalObject gameLocalObject:DaoConfiguration.getInstance().getGameLocalObjectDao().loadAll()){
            RunLocalObject runLocalObject = new RunLocalObject();
            runLocalObject.setGameId(gameLocalObject.getId());
            runLocalObject.setTitle("Default");
            runLocalObject.setDeleted(false);
            DaoConfiguration.getInstance().getRunLocalObjectDao().insertOrReplace(runLocalObject);
        }

    }

    private void createDefaultAccount() {
        AccountLocalObject defaultAccount = new AccountLocalObject();
        defaultAccount.setAccountLevel(0);
        defaultAccount.setEmail("default@mail.com");
        defaultAccount.setName("Anonymous");
        defaultAccount.setFamilyName("Anonymous");
        defaultAccount.setGivenName("Anonymous");
        defaultAccount.setFullId("0:0");
        defaultAccount.setAccountType(0);
        defaultAccount.setLocalId("0");
        defaultAccount.setId(1l);
        DaoConfiguration.getInstance().getAccountLocalObjectDao().insertOrReplace(defaultAccount);
        AccountDelegator.getInstance().setAccount(defaultAccount);
        ARL.properties.setAccount(defaultAccount.getId());
    }

    public class AsyncStartInitDB{

    }

    public class SyncReady{
        private boolean success;

        public SyncReady(boolean success) {
            this.success = success;
        }

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }
    }
}
