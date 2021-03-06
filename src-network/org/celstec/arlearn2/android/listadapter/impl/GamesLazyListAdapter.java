package org.celstec.arlearn2.android.listadapter.impl;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import daoBase.DaoConfiguration;
import de.greenrobot.dao.query.QueryBuilder;
import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.delegators.ARL;
import org.celstec.arlearn2.android.events.GameEvent;
import org.celstec.arlearn2.android.listadapter.LazyListAdapter;
import org.celstec.dao.gen.*;

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
public class GamesLazyListAdapter extends LazyListAdapter<GameLocalObject> {

    private QueryBuilder qb;
    private GamesLazyListAdapter adapterInq;

    public GamesLazyListAdapter(Context context) {
        super(context);
        GameLocalObjectDao dao = DaoConfiguration.getInstance().getGameLocalObjectDao();
        qb = dao.queryBuilder().orderAsc(GameLocalObjectDao.Properties.Title);
        ARL.eventBus.register(this);
        setLazyList(qb.listLazy());
    }

    public GamesLazyListAdapter(Context context, boolean showDeleted) {
        super(context);
        GameLocalObjectDao dao = DaoConfiguration.getInstance().getGameLocalObjectDao();
        qb = dao.queryBuilder().orderAsc(GameLocalObjectDao.Properties.Title);
        qb.where(GameLocalObjectDao.Properties.Deleted.eq(showDeleted));
        ARL.eventBus.register(this);
        setLazyList(qb.listLazy());
    }


    public void onEventMainThread(GameEvent event) {
        if (lazyList != null) lazyList.close();
        setLazyList(qb.listLazy());
        notifyDataSetChanged();
    }

    public void close() {
        if (lazyList != null)lazyList.close();
        ARL.eventBus.unregister(this);
    }

    @Override
    public View newView(Context context, GameLocalObject item, ViewGroup parent) {
        if (item == null) return null;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return inflater.inflate(R.layout.store_game_list_entry_small, parent, false);

    }
    @Override
    public void bindView(View view, Context context,  GameLocalObject item) {
        TextView firstLineView =(TextView) view.findViewById(R.id.gameTitleId);
        firstLineView.setText(item.getTitle());
//        TextView secondLineView =(TextView) view.findViewById(R.id.gameDescriptionId);
//        String description = item.getDescription()==null?"":item.getDescription();
//        for (RunLocalObject run: item.getRuns()) {
//            description+= " run :"+run.getTitle();
//        }
//        for (GameContributorLocalObject owner: item.getContributors()){
//            description += " owner "+owner.getType()+":"+owner.getAccountLocalObject().getName();
//        }
//        secondLineView.setText(description + " id " +item.getId() );
    }


    @Override
    public long getItemId(int position) {
        if (dataValid && lazyList != null) {
            GameLocalObject item = lazyList.get(position);
            if (item != null) {
                return item.getId();
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }

}

