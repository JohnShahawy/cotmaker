/*
MIT License

Copyright (c) 2022 Nic Cellular

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
package com.atakmap.android.cotmaker.plugin;

import com.atakmap.android.ipc.AtakBroadcast;
import com.atakmap.android.cotmaker.cotmakerDropDownReceiver;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.ViewGroup;
import transapps.mapi.MapView;
import transapps.maps.plugin.tool.Group;
import transapps.maps.plugin.tool.Tool;
import transapps.maps.plugin.tool.ToolDescriptor;

public class cotmakerTool extends Tool implements ToolDescriptor {

    private final Context context;

    public cotmakerTool(Context context) {
        this.context = context;
    }

    @Override
    public String getDescription() {
        return context.getString(R.string.app_name);
    }

    @Override
    public Drawable getIcon() {
        return (context == null) ? null
                : context.getResources().getDrawable(R.drawable.ic_launcher);
    }

    @Override
    public Group[] getGroups() {
        return new Group[] {
                Group.GENERAL
        };
    }

    @Override
    public String getShortDescription() {
        return context.getString(R.string.app_name);
    }

    @Override
    public Tool getTool() {
        return this;
    }

    @Override
    public void onActivate(Activity arg0, MapView arg1, ViewGroup arg2,
            Bundle arg3,
            ToolCallback arg4) {

        // Hack to close the dropdown that automatically opens when a tool
        // plugin is activated.
        if (arg4 != null) {
            arg4.onToolDeactivated(this);
        }
        // Intent to launch the dropdown or tool

        //arg2.setVisibility(ViewGroup.INVISIBLE);
        Intent i = new Intent(
                cotmakerDropDownReceiver.SHOW_PLUGIN);
        AtakBroadcast.getInstance().sendBroadcast(i);
    }

    @Override
    public void onDeactivate(ToolCallback arg0) {
    }
}
