package org.celstec.arlearn2.android.delegators.game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


import android.util.Log;
import org.celstec.arlearn2.beans.deserializer.json.JsonBeanDeserializer;
import org.celstec.arlearn2.beans.game.Game;
import org.celstec.arlearn2.beans.game.GameFileList;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.codehaus.jettison.json.JSONTokener;

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
public class GameFilePackageParser {

    private JSONObject fileReferences;

    public GameFilePackageParser(InputStream gamePackageStream){
        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(gamePackageStream));
            StringBuilder builder = new StringBuilder();
            for (String line = null; (line = reader.readLine()) != null;) {
                builder.append(line).append("\n");
            }
            JSONTokener tokenizer = new JSONTokener(builder.toString());
            fileReferences = new JSONObject(tokenizer);
        } catch (IOException e) {
            Log.e("ConfigAdapter", e.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public GameFileList getGameFiles(){
         return (GameFileList) JsonBeanDeserializer.deserialize(fileReferences);
    }

  }
