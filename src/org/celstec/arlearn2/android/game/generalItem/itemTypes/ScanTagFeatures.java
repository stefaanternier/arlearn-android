package org.celstec.arlearn2.android.game.generalItem.itemTypes;

import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.game.generalItem.GeneralItemActivity;
import org.celstec.arlearn2.android.game.generalItem.GeneralItemActivityFeatures;
import org.celstec.arlearn2.android.game.generalItem.GeneralItemMapper;
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
public class ScanTagFeatures extends GeneralItemActivityFeatures {

    @Override
    protected int getImageResource() {
        return GeneralItemMapper.mapConstantToDrawable(GeneralItemMapper.SCAN_TAG);
    }
    @Override
    protected boolean showDataCollection() {
        return false;
    }

    public ScanTagFeatures(GeneralItemActivity activity, GeneralItemLocalObject generalItemLocalObject) {
        super(activity, generalItemLocalObject);
    }
}