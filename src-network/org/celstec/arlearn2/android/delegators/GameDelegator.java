package org.celstec.arlearn2.android.delegators;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;
import daoBase.DaoConfiguration;
import org.celstec.arlearn2.android.delegators.game.GameDownloadManager;
import org.celstec.arlearn2.android.delegators.game.GamePackageParser;
import org.celstec.arlearn2.android.delegators.game.Rating;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.android.download.FileByteDownloader;
import org.celstec.arlearn2.android.events.GameEvent;
import org.celstec.arlearn2.android.events.SearchResultList;

import org.celstec.arlearn2.android.util.FileDownloader;
import org.celstec.arlearn2.android.util.MediaFolders;
import org.celstec.arlearn2.beans.game.*;
import org.celstec.arlearn2.beans.generalItem.GeneralItemList;
import org.celstec.arlearn2.client.GameClient;
import org.celstec.dao.gen.AccountLocalObject;
import org.celstec.dao.gen.GameContributorLocalObject;
import org.celstec.dao.gen.GameFileLocalObject;
import org.celstec.dao.gen.GameLocalObject;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.codehaus.jettison.json.JSONTokener;

import java.io.*;
import java.net.MalformedURLException;
import java.util.Iterator;
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
public final class GameDelegator extends AbstractDelegator{

    private static GameDelegator instance;

    private static long lastSyncDateParticipate = 0l;
    private static long lastSyncDate = 0l;
    public Rating rating = new Rating();

    private GameDelegator() {
        ARL.eventBus.register(this);
    }

    public static GameDelegator getInstance() {
        if (instance == null) {
            instance = new GameDelegator();
        }
        return instance;
    }

     /*
    Public API
     */

    public void syncGameWithoutToken(Long gameId) {
        ARL.eventBus.post(new SyncGameNoToken(gameId));
    }

    public void syncGameFiles(Long gameId) { //TODO check if this method is still needed
        ARL.eventBus.post(new SyncGameFiles(gameId));

    }

//    public GameDownloadManager downloadGame(Long gameId) {
//        GameDownloadManager gm  = new GameDownloadManager(gameId);
//        ARL.eventBus.post(gm);
//        return gm;
//    }

    public void search(String query) {
        ARL.eventBus.post(new SearchGames(query));
    }

    public void search(Double lat, Double lng, Long distance) {
        ARL.eventBus.post(new LocationSearchGames(lat, lng, distance));
    }

    public GameLocalObject asyncGame(long gameId, boolean withToken) {
        String token = returnTokenIfOnline();
        if (token != null || !withToken) {
            Game game = GameClient.getGameClient().getGame(token, gameId);
            if (game.getError() == null) {
                return process(game);
            } else {
                ARL.eventBus.post(GameEvent.syncError());
            }
        }
        return null;
    }

    public void asyncDownloadGame(GameDownloadManager gm) {
        String token = returnTokenIfOnline();
        if (token != null) {
            gm.asyncDownloadGame();
        }

    }

    public Game asyncGameBean(long gameId) {
        String token = returnTokenIfOnline();
        if (token != null) {
            return GameClient.getGameClient().getGame(token, gameId);
        }
        return null;
    }

    public void loadGameFromFile(Context context, Long gameId) {
        AssetManager assetManager = context.getAssets();
        try {
            InputStream inputStream = assetManager.open("game."+gameId+".json");
            GamePackageParser gamePackageParser = new GamePackageParser(inputStream);
            Game game = gamePackageParser.getGameLocalObject();
            GameLocalObject gameLocalObject = toDaoLocalObject(game);
            DaoConfiguration.getInstance().getGameLocalObjectDao().insertOrReplace(gameLocalObject);
            gamePackageParser.getGeneralItems();
            GeneralItemList generalItemList = gamePackageParser.getGeneralItems();
            GeneralItemDelegator.getInstance().process(generalItemList, gameLocalObject, false);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /*
   Implementation
    */

    private void onEventAsync(SyncGameNoToken g) {
        asyncGame(g.getGameId(), false);
    }

    private void onEventAsync(GameDownloadManager g) {
        asyncDownloadGame(g);
    }

    private void onEventAsync(SearchGames sg) {
        if (ARL.isOnline()) {
            GamesList result = GameClient.getGameClient().search(null, sg.query);
            ARL.eventBus.post(new SearchResultList(result));
        }
    }

    private void onEventAsync(LocationSearchGames sg) {
        String token = returnTokenIfOnline();
        if (token != null) {
            GamesList result = GameClient.getGameClient().search(token, sg.getLat(), sg.getLng(), sg.getDistance());
            for (Game gameResult: result.getGames()) {
                process(gameResult);
            }
            ARL.eventBus.post(new SearchResultList(result));
        }
    }

    private void onEventAsync(SyncGamesEventParticipate sge) {
        String token = returnTokenIfOnline();
        if (token != null) {
                GamesList gl = GameClient.getGameClient().getGamesParticipate(token, lastSyncDateParticipate);
                if (gl.getError() == null) {
                    process(gl);
                    lastSyncDateParticipate = gl.getServerTime();
                }

        }
    }

    private void onEventAsync(SyncGamesEvent sge) {
        String token = returnTokenIfOnline();
        if (token != null) {
                GamesList gl = GameClient.getGameClient().getGamesParticipate(token, lastSyncDate);
                if (gl.getError() == null) {
                   process(gl);
                   lastSyncDate = gl.getServerTime();
                }
        }
    }

    private void onEventAsync(SyncGameContributors syncGameContributors) {
        syncGameContributors.sync();
    }

    public void syncGamesParticipate() {
        ARL.eventBus.post(new SyncGamesEventParticipate());
    }

    public void syncMyGames() {
        ARL.eventBus.post(new SyncGamesEvent());
    }

    public void syncContributors(GameLocalObject existingGame, GameLocalObject newGame) {

    }

    private void process(GamesList gl) {
        for (Game gBean : gl.getGames()) {
            process(gBean);
        }
    }

    private GameLocalObject process(Game gBean) {
            GameLocalObject existingGame = DaoConfiguration.getInstance().getGameLocalObjectDao().load(gBean.getGameId());
            GameLocalObject newGame = toDaoLocalObject(gBean);
            newGame.setIcon(new FileByteDownloader(ARL.config.getProperty("arlearn_server")+"/game/"+gBean.getGameId()+"/gameThumbnail?thumbnail=200&crop=true").syncDownload());
            if ( (existingGame == null || newGame.getLastModificationDate() > existingGame.getLastModificationDate())) {
                DaoConfiguration.getInstance().getGameLocalObjectDao().insertOrReplace(newGame);
                ARL.eventBus.post(new SyncGameContributors(existingGame, newGame));
//                DaoConfiguration.getInstance().getGameLocalObjectDao().insertOrReplace(toDaoLocalObject(gBean));
                return newGame;
            }

        return existingGame;

    }

    private GameLocalObject toDaoLocalObject(Game gBean) {
        GameLocalObject gameDao = new GameLocalObject();
        gameDao.setId(gBean.getGameId());
        gameDao.setTitle(gBean.getTitle());
        gameDao.setDeleted(gBean.getDeleted());
        gameDao.setDescription(gBean.getDescription());
        gameDao.setLicenseCode(gBean.getLicenseCode());
        gameDao.setLastModificationDate(gBean.getLastModificationDate());
        if (gameDao.getLastModificationDate() == null) gameDao.setLastModificationDate(0l);
        gameDao.setLat(gBean.getLat());
        gameDao.setLng(gBean.getLng());
        gameDao.setBean(gBean.toString());
        return gameDao;
    }

    public void deleteGames() {
        lastSyncDate = 0l;
        DaoConfiguration.getInstance().getGameLocalObjectDao().deleteAll();
    }

    private void onEventAsync(SyncGameFiles gameFiles) {
        asyncRetrieveGameFiles(gameFiles.getGameId());
    }

    public GameFileList asyncRetrieveGameFiles(long gameId) {
        String token = returnTokenIfOnline();
        if (token != null) {
            boolean reset = false;
            for (GameFile gf: GameClient.getGameClient().getGameFileList(token, gameId).getGameFiles()) {
                GameFileLocalObject gameFileLocalObject = DaoConfiguration.getInstance().getGameFileDao().load(gf.getId());

                if (gameFileLocalObject == null) {
                    gameFileLocalObject = new GameFileLocalObject(gf);
                    if (gf.getPath().contains("/generalItems/")) {
                        String itemId = gf.getPath().substring(14);
                        itemId = itemId.substring(0, itemId.indexOf("/"));
                        gameFileLocalObject.setGeneralItem(Long.parseLong(itemId));
                    }
                    gameFileLocalObject.setGameId(gameId);
                    gameFileLocalObject.setSyncStatus(GameFileLocalObject.FILE_TO_DOWNLOAD);
                    DaoConfiguration.getInstance().getGameFileDao().insertOrReplace(gameFileLocalObject);
                    reset = true;
                } else {
                    if (gf.getDeleted() != null && gf.getDeleted() && !gameFileLocalObject.getDeleted()) {
                        gameFileLocalObject.setDeleted(true);
                        DaoConfiguration.getInstance().getGameFileDao().insertOrReplace(gameFileLocalObject);
                    }
                }
            }
            if (reset) {
                DaoConfiguration.getInstance().getGameLocalObjectDao().load(gameId).resetGameFiles();
            }
        }
        return null;
    }

    public void asyncDownloadGameContent(long gameId) {
        List<GameFileLocalObject> gameFiles = DaoConfiguration.getInstance().getGameLocalObjectDao().load(gameId).getGameFiles();
        for (GameFileLocalObject gameFileLocalObject : gameFiles) {
            if (gameFileLocalObject.getSyncStatus() == GameFileLocalObject.FILE_TO_DOWNLOAD){
                try {
                    File targetFile = new File(MediaFolders.getIncommingFilesDir(), gameId+gameFileLocalObject.getPath());
                    MediaFolders.createFileFoldersRecursively(targetFile);
                    FileDownloader fd = new FileDownloader(ARL.config.getProperty("arlearn_server")+"game/"+gameId+gameFileLocalObject.getPath(), targetFile);
                    gameFileLocalObject.setSyncStatus(GameFileLocalObject.FILE_IS_DOWNLOADING);
                    DaoConfiguration.getInstance().getGameFileDao().insertOrReplace(gameFileLocalObject);
                    fd.download();
                    if (fd.getTargetLocation().exists()) {
                        gameFileLocalObject.setSyncStatus(GameFileLocalObject.FILE_DOWNLOADED);
                        DaoConfiguration.getInstance().getGameFileDao().insertOrReplace(gameFileLocalObject);
                    }
                } catch (MalformedURLException e) {
                    Log.e("ARLearn", e.getMessage(), e);
                } catch (FileNotFoundException e) {
                    Log.e("ARLearn", e.getMessage(), e);
                    gameFileLocalObject.setSyncStatus(GameFileLocalObject.FILE_TO_DOWNLOAD);
                    DaoConfiguration.getInstance().getGameFileDao().insertOrReplace(gameFileLocalObject);
                }
            }
        }
    }

    private class SyncGameNoToken {
        private long gameId;

        private SyncGameNoToken(long gameId) {
            this.gameId = gameId;
        }

        public long getGameId() {
            return gameId;
        }

        public void setGameId(long gameId) {
            this.gameId = gameId;
        }
    }

//    private class DownloadGame {
//        private long gameId;
//
//        private DownloadGame(long gameId) {
//            this.gameId = gameId;
//        }
//
//        public long getGameId() {
//            return gameId;
//        }
//
//        public void setGameId(long gameId) {
//            this.gameId = gameId;
//        }
//    }

    private class SyncGamesEventParticipate {

    }

    private class SyncGamesEvent {
    }

    private class SyncGameContributors {
        private GameLocalObject existingGame;
        private GameLocalObject newGame;

        public SyncGameContributors(GameLocalObject existingGame, GameLocalObject newGame) {
            this.existingGame = existingGame;
            this.newGame = newGame;
        }

        public GameLocalObject getExistingGame() {
            return existingGame;
        }

        public void setExistingGame(GameLocalObject existingGame) {
            this.existingGame = existingGame;
        }

        public GameLocalObject getNewGame() {
            return newGame;
        }

        public void setNewGame(GameLocalObject newGame) {
            this.newGame = newGame;
        }

        public void sync() {
            if (existingGame != null) {
                synchronized (existingGame){
                    Iterator<GameContributorLocalObject> iterator = existingGame.getContributors().iterator();
                    while (iterator.hasNext()) {
                        GameContributorLocalObject gameContributorLocalObject = iterator.next();
                        DaoConfiguration.getInstance().getGameContributorLocalObjectDao().delete(gameContributorLocalObject);
                        iterator.remove();
                    }
//                    for (GameContributorLocalObject gameContributorLocalObject: existingGame.getContributors()) {
//                        DaoConfiguration.getInstance().getGameContributorLocalObjectDao().delete(gameContributorLocalObject);
//                        existingGame.getContributors().remove(gameContributorLocalObject);
//                    }
                }
            }
            newGame.resetContributors();
            DaoConfiguration.getInstance().getGameLocalObjectDao().insertOrReplace(newGame);
            PropertiesAdapter pa = PropertiesAdapter.getInstance();
            GameAccessList gameAccessList =  GameClient.getGameClient().getGameAccessList(pa.getAuthToken(), newGame.getId());
            for (GameAccess gameAccess: gameAccessList.getGameAccess()) {
                Log.e("TEST", ""+gameAccess.getAccount());
                AccountLocalObject account = ARL.accounts.getAccount(gameAccess.getAccount());
                if  (account == null) {
                    account = ARL.accounts.asyncAccountLocalObject(gameAccess.getAccount());
                }
                if (account != null) {
                        GameContributorLocalObject contributor = new GameContributorLocalObject();
                        contributor.setAccountLocalObject(account);
                        contributor.setType(gameAccess.getAccessRights());
                        contributor.setGameLocalObject(newGame);
                        DaoConfiguration.getInstance().getGameContributorLocalObjectDao().insertOrReplace(contributor);
//                        newGame.getContributors().add(contributor);
                        DaoConfiguration.getInstance().getGameContributorLocalObjectDao().update(contributor);
                }
                Log.e("TEST", "" + gameAccess.getAccessRights());
            }
            DaoConfiguration.getInstance().getGameLocalObjectDao().insertOrReplace(newGame);
            ARL.eventBus.post(new GameEvent(newGame.getId()));
        }
    }

    private class SearchGames {
        private String query;

        private SearchGames(String query) {
            this.query = query;
        }

        public String getQuery() {
            return query;
        }

        public void setQuery(String query) {
            this.query = query;
        }
    }

    private class SyncGameFiles {
        private long gameId;

        private SyncGameFiles(long gameId) {
            this.gameId = gameId;
        }

        public long getGameId() {
            return gameId;
        }

        public void setGameId(long gameId) {
            this.gameId = gameId;
        }
    }
    private class LocationSearchGames {
        private Double lat;
        private Double lng;
        private Long distance;

        private LocationSearchGames(Double lat, Double lng, Long distance) {
            this.lat = lat;
            this.lng = lng;
            this.distance = distance;
        }

        public Double getLat() {
            return lat;
        }

        public void setLat(Double lat) {
            this.lat = lat;
        }

        public Double getLng() {
            return lng;
        }

        public void setLng(Double lng) {
            this.lng = lng;
        }

        public Long getDistance() {
            return distance;
        }

        public void setDistance(Long distance) {
            this.distance = distance;
        }
    }

}
