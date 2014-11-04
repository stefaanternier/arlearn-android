package org.celstec.dao.gen;

import java.util.List;
import java.util.ArrayList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.SqlUtils;
import de.greenrobot.dao.internal.DaoConfig;
import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.QueryBuilder;

import org.celstec.dao.gen.GameCategoryLocalObject;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table GAME_CATEGORY_LOCAL_OBJECT.
*/
public class GameCategoryLocalObjectDao extends AbstractDao<GameCategoryLocalObject, Long> {

    public static final String TABLENAME = "GAME_CATEGORY_LOCAL_OBJECT";

    /**
     * Properties of entity GameCategoryLocalObject.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Deleted = new Property(1, Boolean.class, "deleted", false, "DELETED");
        public final static Property CategoryId = new Property(2, Long.class, "categoryId", false, "CATEGORY_ID");
        public final static Property GameId = new Property(3, Long.class, "gameId", false, "GAME_ID");
    };

    private DaoSession daoSession;

    private Query<GameCategoryLocalObject> categoryLocalObject_GamesQuery;

    public GameCategoryLocalObjectDao(DaoConfig config) {
        super(config);
    }
    
    public GameCategoryLocalObjectDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'GAME_CATEGORY_LOCAL_OBJECT' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'DELETED' INTEGER," + // 1: deleted
                "'CATEGORY_ID' INTEGER," + // 2: categoryId
                "'GAME_ID' INTEGER);"); // 3: gameId
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'GAME_CATEGORY_LOCAL_OBJECT'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, GameCategoryLocalObject entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Boolean deleted = entity.getDeleted();
        if (deleted != null) {
            stmt.bindLong(2, deleted ? 1l: 0l);
        }
 
        Long categoryId = entity.getCategoryId();
        if (categoryId != null) {
            stmt.bindLong(3, categoryId);
        }
 
        Long gameId = entity.getGameId();
        if (gameId != null) {
            stmt.bindLong(4, gameId);
        }
    }

    @Override
    protected void attachEntity(GameCategoryLocalObject entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public GameCategoryLocalObject readEntity(Cursor cursor, int offset) {
        GameCategoryLocalObject entity = new GameCategoryLocalObject( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getShort(offset + 1) != 0, // deleted
            cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2), // categoryId
            cursor.isNull(offset + 3) ? null : cursor.getLong(offset + 3) // gameId
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, GameCategoryLocalObject entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setDeleted(cursor.isNull(offset + 1) ? null : cursor.getShort(offset + 1) != 0);
        entity.setCategoryId(cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2));
        entity.setGameId(cursor.isNull(offset + 3) ? null : cursor.getLong(offset + 3));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(GameCategoryLocalObject entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(GameCategoryLocalObject entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
    /** Internal query to resolve the "games" to-many relationship of CategoryLocalObject. */
    public List<GameCategoryLocalObject> _queryCategoryLocalObject_Games(Long categoryId) {
        synchronized (this) {
            if (categoryLocalObject_GamesQuery == null) {
                QueryBuilder<GameCategoryLocalObject> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.CategoryId.eq(null));
                categoryLocalObject_GamesQuery = queryBuilder.build();
            }
        }
        Query<GameCategoryLocalObject> query = categoryLocalObject_GamesQuery.forCurrentThread();
        query.setParameter(0, categoryId);
        return query.list();
    }

    private String selectDeep;

    protected String getSelectDeep() {
        if (selectDeep == null) {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T0", daoSession.getStoreGameLocalObjectDao().getAllColumns());
            builder.append(" FROM GAME_CATEGORY_LOCAL_OBJECT T");
            builder.append(" LEFT JOIN STORE_GAME_LOCAL_OBJECT T0 ON T.'GAME_ID'=T0.'_id'");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected GameCategoryLocalObject loadCurrentDeep(Cursor cursor, boolean lock) {
        GameCategoryLocalObject entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        StoreGameLocalObject storeGameLocalObject = loadCurrentOther(daoSession.getStoreGameLocalObjectDao(), cursor, offset);
        entity.setStoreGameLocalObject(storeGameLocalObject);

        return entity;    
    }

    public GameCategoryLocalObject loadDeep(Long key) {
        assertSinglePk();
        if (key == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder(getSelectDeep());
        builder.append("WHERE ");
        SqlUtils.appendColumnsEqValue(builder, "T", getPkColumns());
        String sql = builder.toString();
        
        String[] keyArray = new String[] { key.toString() };
        Cursor cursor = db.rawQuery(sql, keyArray);
        
        try {
            boolean available = cursor.moveToFirst();
            if (!available) {
                return null;
            } else if (!cursor.isLast()) {
                throw new IllegalStateException("Expected unique result, but count was " + cursor.getCount());
            }
            return loadCurrentDeep(cursor, true);
        } finally {
            cursor.close();
        }
    }
    
    /** Reads all available rows from the given cursor and returns a list of new ImageTO objects. */
    public List<GameCategoryLocalObject> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<GameCategoryLocalObject> list = new ArrayList<GameCategoryLocalObject>(count);
        
        if (cursor.moveToFirst()) {
            if (identityScope != null) {
                identityScope.lock();
                identityScope.reserveRoom(count);
            }
            try {
                do {
                    list.add(loadCurrentDeep(cursor, false));
                } while (cursor.moveToNext());
            } finally {
                if (identityScope != null) {
                    identityScope.unlock();
                }
            }
        }
        return list;
    }
    
    protected List<GameCategoryLocalObject> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<GameCategoryLocalObject> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}
