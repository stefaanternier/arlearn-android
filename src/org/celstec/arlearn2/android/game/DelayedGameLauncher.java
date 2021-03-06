package org.celstec.arlearn2.android.game;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import org.celstec.arlearn2.android.game.messageViews.GameMessages;
import org.celstec.dao.gen.GameLocalObject;
import org.celstec.dao.gen.RunLocalObject;

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
public abstract class DelayedGameLauncher  implements  Runnable {

    private long timeForLaunchNextScreen;
    private Handler handler = new Handler();


    public DelayedGameLauncher(long timeForLaunchNextScreen) {
        this.timeForLaunchNextScreen =timeForLaunchNextScreen;
        postPone();
    }

    public void postPone() {
        handler.postDelayed(this, 500);
    }

    public void run() {
        if (timeForLaunchNextScreen < System.currentTimeMillis() && additionalCondition()) {
            runNextActivity();
        } else {
            postPone();
        }
    }

    public abstract void runNextActivity();

    public abstract boolean additionalCondition();

//    public void run() {
//        Intent gameIntent = new Intent(ctx, GameMessages.class);
//        gameIntent.putExtra(GameLocalObject.class.getName(), gameId);
//        gameIntent.putExtra(RunLocalObject.class.getName(), runId);
//        ctx.startActivity(gameIntent);
//        ctx.finish();
//    }

//    private void launchGame() {
//
//    }
}
