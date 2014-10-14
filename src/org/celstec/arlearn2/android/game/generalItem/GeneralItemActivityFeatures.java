package org.celstec.arlearn2.android.game.generalItem;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.TextView;
import daoBase.DaoConfiguration;
import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.dataCollection.*;
import org.celstec.arlearn2.android.dataCollection.activities.AudioCollectionActivity;
import org.celstec.arlearn2.android.dataCollection.activities.TextInputCollectionActivity;
import org.celstec.arlearn2.android.dataCollection.activities.ValueInputCollectionActivity;
import org.celstec.arlearn2.android.delegators.ARL;
import org.celstec.arlearn2.android.game.generalItem.dataCollection.DataCollectionResultController;
import org.celstec.arlearn2.android.game.generalItem.dataCollection.DataCollectionViewController;
import org.celstec.arlearn2.android.game.generalItem.dataCollection.LazyListAdapter;
import org.celstec.arlearn2.android.game.generalItem.dataCollection.impl.AudioCollectionActivityImpl;
import org.celstec.arlearn2.android.game.generalItem.dataCollection.impl.TextInputCollectionActivityImpl;
import org.celstec.arlearn2.android.game.generalItem.dataCollection.impl.ValueInputCollectionActivityImpl;
import org.celstec.arlearn2.android.game.generalItem.itemTypes.*;
import org.celstec.arlearn2.android.game.messageViews.GameActivityFeatures;
import org.celstec.arlearn2.android.views.DrawableUtil;
import org.celstec.arlearn2.android.views.StyleUtil;
import org.celstec.arlearn2.beans.generalItem.*;
import org.celstec.arlearn2.beans.run.Action;
import org.celstec.dao.gen.GeneralItemLocalObject;

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
public abstract class GeneralItemActivityFeatures {

    public GeneralItemLocalObject generalItemLocalObject;
    public DataCollectionViewController dataCollectionViewController;
    public DataCollectionResultController dataCollectionResultController;
    private LazyListAdapter lazyListAdapter;

    protected GeneralItemActivity activity;
    protected GeneralItem generalItemBean;

    private PictureManager pictureManager;
    private TextInputManager textInputManager;
    private ValueInputManager valueInputManager;
    private AudioInputManager audioInputManager;


    public static GeneralItemActivityFeatures getGeneralItemActivityFeatures(final GeneralItemActivity activity){

        Long generalItemId = activity.getIntent().getLongExtra(GeneralItemLocalObject.class.getName(), 0l);
        GeneralItemLocalObject generalItemLocalObject = DaoConfiguration.getInstance().getGeneralItemLocalObjectDao().load(generalItemId);
        GeneralItemActivityFeatures result = null;
        switch (GeneralItemMapper.mapBeanToConstant(generalItemLocalObject.getGeneralItemBean())){
            case GeneralItemMapper.NARRATOR_ITEM:
                result =new NarratorItemFeatures(activity, generalItemLocalObject);
                break;
            case GeneralItemMapper.SCAN_TAG:
                result = new ScanTagFeatures(activity, generalItemLocalObject);
                break;
            case GeneralItemMapper.SINGLE_CHOICE:
                result = new SingleChoiceFeatures(activity, generalItemLocalObject);
                break;
            case GeneralItemMapper.MULTI_CHOICE:
                result = new MultipleChoiceFeatures(activity, generalItemLocalObject);
                break;
            case GeneralItemMapper.SINGLE_CHOICE_IMAGE:
                result = new SingleChoiceImageTestFeatures(activity, generalItemLocalObject);
                break;
            case GeneralItemMapper.MULTI_CHOICE_IMAGE:
                result = new MultipleChoiceImageTestFeatures(activity, generalItemLocalObject);
                break;
            case GeneralItemMapper.AUDIO_OBJECT:
                result =new AudioItemFeatures(activity, generalItemLocalObject);
                break;
            case GeneralItemMapper.VIDEO_OBJECT:
                result =new VideoObjectFeatures(activity, generalItemLocalObject);
                break;
        }
        result.setMetadata();
        return result;
    }

    protected abstract boolean showDataCollection();

    protected abstract int  getImageResource();

    public GeneralItemActivityFeatures(final GeneralItemActivity activity, GeneralItemLocalObject generalItemLocalObject) {
        this.activity = activity;
        this.generalItemLocalObject = generalItemLocalObject;
        this.generalItemBean = generalItemLocalObject.getGeneralItemBean();
        dataCollectionResultController = new DataCollectionResultController(this.activity);
        lazyListAdapter = new LazyListAdapter(GeneralItemActivityFeatures.this.activity.getGameActivityFeatures().getRunId(), generalItemLocalObject.getId());
        dataCollectionResultController.setAdapter(lazyListAdapter);
        dataCollectionResultController.notifyDataSetChanged();


        dataCollectionViewController = new DataCollectionViewController(activity){

            @Override
            public void onAudioClick() {

                audioInputManager = new AudioInputManager(activity);
                audioInputManager.setGeneralItem(GeneralItemActivityFeatures.this.generalItemLocalObject);
                audioInputManager.setRunId(GeneralItemActivityFeatures.this.activity.getGameActivityFeatures().getRunId());
                audioInputManager.setTheme(activity.getGameActivityFeatures().getTheme());
                audioInputManager.takeDataSample(AudioCollectionActivityImpl.class);
            }

            @Override
            public void onPictureClick() {
                pictureManager = new PictureManager(activity);
                pictureManager.setGeneralItem(GeneralItemActivityFeatures.this.generalItemLocalObject);
                pictureManager.setRunId(GeneralItemActivityFeatures.this.activity.getGameActivityFeatures().getRunId());
                pictureManager.setTheme(activity.getGameActivityFeatures().getTheme());
                pictureManager.takeDataSample(null);
            }

            @Override
            public void onVideoClick() {
                dataCollectionViewController.checkVideo();
            }

            @Override
            public void onTextClick() {
                textInputManager = new TextInputManager(activity);
                textInputManager.setGeneralItem(GeneralItemActivityFeatures.this.generalItemLocalObject);
                textInputManager.setRunId(GeneralItemActivityFeatures.this.activity.getGameActivityFeatures().getRunId());
                textInputManager.setTheme(activity.getGameActivityFeatures().getTheme());

                textInputManager.takeDataSample(TextInputCollectionActivityImpl.class);
            }

            @Override
            public void onNumberClick() {
                valueInputManager = new ValueInputManager(activity);
                valueInputManager.setGeneralItem(GeneralItemActivityFeatures.this.generalItemLocalObject);
                valueInputManager.setRunId(GeneralItemActivityFeatures.this.activity.getGameActivityFeatures().getRunId());
                valueInputManager.setTheme(activity.getGameActivityFeatures().getTheme());

                valueInputManager.takeDataSample(ValueInputCollectionActivityImpl.class);
            }

        };
        if (showDataCollection()) {
            dataCollectionViewController.showDataCollection();
        } else {
            dataCollectionViewController.hideDataCollection();
        }
        if (generalItemLocalObject == null) throw new NullPointerException("General Item object is null");

//
    }

    public void onResumeActivity(){
        dataCollectionViewController.showChecks(lazyListAdapter);
    }

    public void setMetadata(){
        if (DrawableUtil.isInit()) new DrawableUtil(activity.getGameActivityFeatures().getTheme(), activity);
        Drawable iconDrawable = activity.getResources().getDrawable(getImageResource()).mutate();
        TypedArray ta =  activity.obtainStyledAttributes(activity.getGameActivityFeatures().getTheme(), new int[]{R.attr.primaryColor});
        ColorFilter filter = new LightingColorFilter( Color.BLACK, ta.getColor(0, Color.BLACK));
        iconDrawable.setColorFilter(filter);
        ((ImageView)this.activity.findViewById(R.id.generalItemIcon)).setImageDrawable(iconDrawable);
        //((ImageView)this.activity.findViewById(R.id.generalItemIcon)).setImageResource(getImageResource());

        activity.findViewById(R.id.audioButtonIcon).setBackgroundDrawable(DrawableUtil.getPrimaryColorOvalWithState());
        activity.findViewById(R.id.pictureButtonIcon).setBackgroundDrawable(DrawableUtil.getPrimaryColorOvalWithState());
        activity.findViewById(R.id.videoButtonIcon).setBackgroundDrawable(DrawableUtil.getPrimaryColorOvalWithState());
        activity.findViewById(R.id.textButtonIcon).setBackgroundDrawable(DrawableUtil.getPrimaryColorOvalWithState());
        activity.findViewById(R.id.numberButtonIcon).setBackgroundDrawable(DrawableUtil.getPrimaryColorOvalWithState());

        activity.findViewById(R.id.audioButtonCheckIcon).setBackgroundDrawable(DrawableUtil.getButtonAlternativeColorOval());
        activity.findViewById(R.id.pictureButtonCheckIcon).setBackgroundDrawable(DrawableUtil.getButtonAlternativeColorOval());
        activity.findViewById(R.id.videoButtonCheckIcon).setBackgroundDrawable(DrawableUtil.getButtonAlternativeColorOval());
        activity.findViewById(R.id.textButtonCheckIcon).setBackgroundDrawable(DrawableUtil.getButtonAlternativeColorOval());
        activity.findViewById(R.id.numberButtonCheckIcon).setBackgroundDrawable(DrawableUtil.getButtonAlternativeColorOval());

        TextView titleView = (TextView) this.activity.findViewById(R.id.titleId);
        titleView.setText(generalItemLocalObject.getTitle());

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case DataCollectionManager.PICTURE_RESULT:
                pictureManager.onActivityResult(requestCode, resultCode, data);
                break;
            case DataCollectionManager.AUDIO_RESULT:
                audioInputManager.onActivityResult(requestCode, resultCode, data);
                break;
            case DataCollectionManager.VIDEO_RESULT:
                break;
            case DataCollectionManager.TEXT_RESULT:
                textInputManager.onActivityResult(requestCode, resultCode, data);
                break;
            case DataCollectionManager.VALUE_RESULT:
                valueInputManager.onActivityResult(requestCode, resultCode, data);
                break;
        }
        Action action = new Action();
        action.setAction("answer_given");
        action.setRunId(GeneralItemActivityFeatures.this.activity.getGameActivityFeatures().getRunId());
        action.setGeneralItemType(generalItemLocalObject.getGeneralItemBean().getType());
        action.setGeneralItemId(generalItemLocalObject.getId());
        ARL.actions.createAction(action);
        ARL.responses.syncResponses(this.activity.getGameActivityFeatures().getRunId());
    }


    public void onPauseActivity(){}

    public void updateResponses() {
        dataCollectionResultController.notifyDataSetChanged();
    }

    public void updateGeneralItem(){
        generalItemLocalObject =DaoConfiguration.getInstance().getGeneralItemLocalObjectDao().load(generalItemLocalObject.getId());
        generalItemBean = generalItemLocalObject.getGeneralItemBean();
    }
}
