package org.celstec.arlearn2.android.game.generalItem.dataCollection.impl;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.dataCollection.activities.TextInputCollectionActivity;
import org.celstec.arlearn2.android.views.StyleUtil;

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
public class TextInputCollectionActivityImpl extends TextInputCollectionActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GradientDrawable shapeDrawable = (GradientDrawable) ((findViewById(R.id.functionPad)).getBackground());
        shapeDrawable.setColor(new StyleUtil(this, R.style.ARLearn_schema1).getPrimaryColor());

    }
    @Override
    public int getGameGeneralItemDcTextInput() {
        return R.layout.game_general_item_dc_text_input;
    }

    @Override
    public int getDataCollectionText() {
        return R.id.dataCollectionText;
    }

    @Override
    public int getDataCollectionSubmit() {
        return R.id.dataCollectionSubmit;
    }
}
