package org.celstec.arlearn2.android.gcm.handlers;

import android.content.Context;
import org.celstec.arlearn2.android.delegators.GameDelegator;
import org.celstec.arlearn2.android.gcm.NotificationListenerInterface;
import org.celstec.arlearn2.beans.game.Game;

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
public class GCMGameHandler implements NotificationListenerInterface {

    @Override
    public boolean acceptNotificationType(String notificationType) {
        return Game.class.getName().equals(notificationType);
    }

    @Override
    public void handleNotification(HashMap<String,String> map) {
        GameDelegator.getInstance().syncGamesParticipate();
    }
}