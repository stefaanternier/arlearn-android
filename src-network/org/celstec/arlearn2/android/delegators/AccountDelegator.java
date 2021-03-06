package org.celstec.arlearn2.android.delegators;

import android.app.Activity;
import daoBase.DaoConfiguration;
import de.greenrobot.dao.query.QueryBuilder;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.celstec.arlearn2.android.db.Constants;
import org.celstec.arlearn2.android.events.MyAccount;
import org.celstec.arlearn2.android.gcm.GCMRegisterTask;
import org.celstec.arlearn2.beans.account.Account;
import org.celstec.arlearn2.client.AccountClient;
import org.celstec.arlearn2.client.CollaborationClient;
import org.celstec.dao.gen.AccountLocalObject;
import org.celstec.dao.gen.AccountLocalObjectDao;
import org.celstec.dao.gen.GameLocalObjectDao;

import java.io.InputStream;
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
public class AccountDelegator extends AbstractDelegator{

    private static AccountDelegator instance;
    private AccountLocalObject loggedInAccount;

    private AccountDelegator() {
        ARL.eventBus.register(this);
    }

    public static AccountDelegator getInstance() {
        if (instance == null) {
            instance = new AccountDelegator();
        }
        return instance;
    }

    public void syncMyAccountDetails() {
        long accountId = ARL.properties.getAccount();
        if (accountId != 0) {
            loggedInAccount = DaoConfiguration.getInstance().getAccountLocalObjectDao().load(accountId);
            ARL.accountSynchronisationComplete();
        }
        if (loggedInAccount == null) {
            ARL.eventBus.post(new SyncMyAccount());
        }
    }

    public void syncAccount(String fullId) {
        ARL.eventBus.post(new SyncAccount(fullId));
    }

    public AccountLocalObject getLoggedInAccount(){
        return loggedInAccount;
    }

    public void setAccount(AccountLocalObject defaultAccount) {
        loggedInAccount = defaultAccount;
    }


    public void onEventAsync(SyncMyAccount sma) {
        String token = returnTokenIfOnline();
        if (token != null && !"".equals(token)) {
            Account account = AccountClient.getAccountClient().accountDetails(token);
            loggedInAccount = syncAccount(account);
            if (loggedInAccount!=null) {
                ARL.eventBus.post(new MyAccount(loggedInAccount));
                DaoConfiguration.getInstance().getAccountLocalObjectDao().insertOrReplace(loggedInAccount);
                ARL.properties.setAccount(loggedInAccount.getId());
                ARL.properties.setFullId(loggedInAccount.getFullId());
                ARL.accountSynchronisationComplete();
            }
        }
    }

    public void disAuthenticate() {
        ARL.properties.setAuthToken(null);
        ARL.properties.setAccount(0l);
        ARL.games.deleteGames();
        loggedInAccount = null;
    }

    public boolean isAuthenticated(){
        return loggedInAccount != null;
    }

    public AccountLocalObject getAccount(String accountFullId) {
        AccountLocalObjectDao dao = DaoConfiguration.getInstance().getAccountLocalObjectDao();
        List<AccountLocalObject> resultList = dao.queryBuilder().where(AccountLocalObjectDao.Properties.FullId.eq(accountFullId)).list();
        if (resultList.isEmpty()) return null;
        return resultList.get(0);
    }

    public void onEventAsync(SyncAccount sa) {
        String token = returnTokenIfOnline();
        if (token != null) {
            Account account = CollaborationClient.getAccountClient().getContact(token, sa.getFullId());
            DaoConfiguration.getInstance().getAccountLocalObjectDao().insertOrReplace(syncAccount(account));
        }
    }

    public AccountLocalObject asyncAccountLocalObject(String fullId) {
        String token = returnTokenIfOnline();
        if (token != null) {
            Account account = CollaborationClient.getAccountClient().getContact(token, fullId);
            AccountLocalObject resultObject = syncAccount(account);
            DaoConfiguration.getInstance().getAccountLocalObjectDao().insertOrReplace(resultObject);
            return resultObject;
        }
        return null;
    }

    private AccountLocalObject syncAccount(Account account) {
        AccountLocalObject localObject = getAccount(account.getFullId());
        localObject = toDaoLocalObject(account, localObject);
        return localObject;
    }

    private AccountLocalObject toDaoLocalObject(Account aBean, AccountLocalObject accountDao) {
        if (accountDao == null) accountDao = new AccountLocalObject();
        accountDao.setName(aBean.getName());
        accountDao.setFamilyName(aBean.getFamilyName());
        accountDao.setGivenName(aBean.getGivenName());
        accountDao.setFullId(aBean.getFullId());
        accountDao.setAccountLevel(aBean.getAccountLevel());
        accountDao.setAccountType(aBean.getAccountType());
        accountDao.setLocalId(aBean.getLocalId());
        accountDao.setPicture(downloadImage(aBean.getPicture()));
        accountDao.setEmail(aBean.getEmail());
        return accountDao;
    }

    private byte[] downloadImage(String imageUrl){
        try {
            DefaultHttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet(imageUrl);
            HttpResponse response = client.execute(request);
            HttpEntity entity = response.getEntity();
            int imageLength = (int)(entity.getContentLength());
            InputStream is = entity.getContent();

            byte[] imageBlob = new byte[imageLength];
            int bytesRead = 0;
            while (bytesRead < imageLength) {
                int n = is.read(imageBlob, bytesRead, imageLength - bytesRead);
                if (n <= 0)
                    ; // do some error handling
                bytesRead += n;
            }
            return imageBlob;
        }catch (Exception e) {
            return null;
        }
    }


    private class SyncMyAccount{}

    private class SyncAccount {
        private String fullId;

        private SyncAccount(String fullId) {
            this.fullId = fullId;
        }

        public String getFullId() {
            return fullId;
        }

        public void setFullId(String fullId) {
            this.fullId = fullId;
        }
    }

    public AccountLocalObject createOrRetrieveAccount(int accountType, String accountLocalId) {
        DaoConfiguration daoConfiguration= DaoConfiguration.getInstance();
        try {
            QueryBuilder<AccountLocalObject> qb =daoConfiguration.getSession().queryBuilder(AccountLocalObject.class);
            qb.where(AccountLocalObjectDao.Properties.FullId.eq(accountType+":"+accountLocalId));
            List<AccountLocalObject> accounts = qb.list();
            for (AccountLocalObject ac : accounts) {
                return ac;
            }

        }catch (NullPointerException e) {

        }
        AccountLocalObject account = ARL.accounts.asyncAccountLocalObject(accountType+":"+accountLocalId);

//        AccountLocalObject account = new AccountLocalObject();
//        account.setLocalId(accountLocalId);
//        account.setAccountType(accountType);
//        account.setName(accountLocalId);

//        DaoConfiguration.getInstance().getAccountLocalObjectDao().insertOrReplace(account);
        return account;
    }
}
