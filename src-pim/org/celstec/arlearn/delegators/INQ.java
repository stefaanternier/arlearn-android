package org.celstec.arlearn.delegators;

import android.content.Context;
import org.celstec.arlearn2.android.delegators.ARL;
import org.celstec.arlearn2.android.delegators.GameDelegator;
import org.celstec.arlearn2.android.delegators.GeneralItemDelegator;

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
public class INQ extends ARL {

    public static InquiryDelegator inquiry;
    public static BadgesDelegator badges;
    public static QuestionDelegator questions;
    public static DataCollectionTaskDelegator dataCollection;
    public static FriendsDelegator friendsDelegator;

    public static void init(Context ctx) {
        ARL.init(ctx);
        inquiry = InquiryDelegator.getInstance();
        badges = BadgesDelegator.getInstance();
        questions = QuestionDelegator.getInstance();
        dataCollection = DataCollectionTaskDelegator.getInstance();
        friendsDelegator = FriendsDelegator.getInstance();
    }


}
