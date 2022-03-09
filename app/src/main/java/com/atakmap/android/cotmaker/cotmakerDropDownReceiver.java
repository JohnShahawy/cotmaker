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
package com.atakmap.android.cotmaker;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import android.widget.RadioButton;
import android.widget.Toast;

import com.atakmap.android.cot.CotMapComponent;
import com.atakmap.android.gui.EditText;
import com.atakmap.coremap.cot.event.CotDetail;
import com.atakmap.coremap.cot.event.CotEvent;
import com.atakmap.coremap.cot.event.CotPoint;
import com.atakmap.android.maps.MapView;
import com.atakmap.android.cotmaker.plugin.R;
import com.atakmap.android.dropdown.DropDown.OnStateListener;
import com.atakmap.android.dropdown.DropDownReceiver;

import com.atakmap.coremap.log.Log;
import com.atakmap.coremap.maps.time.CoordinatedTime;

import java.util.Locale;


public class cotmakerDropDownReceiver extends DropDownReceiver implements
        OnStateListener {

    public static final String TAG = "cotmakerDropDownReceiver";

    public static final String SHOW_PLUGIN = "com.atakmap.android.cotmaker.SHOW_PLUGIN";

    private final Context pluginContext;
    private final View mainView;
    private Button submit;
    private EditText coords;
    private RadioButton type;

    /**************************** CONSTRUCTOR *****************************/

    public cotmakerDropDownReceiver(final MapView mapView,
            final Context context) {
        super(mapView);
        this.pluginContext = context;

        LayoutInflater inflater = (LayoutInflater) context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mainView = inflater.inflate(R.layout.main_layout, null);
    }

    /**************************** PUBLIC METHODS *****************************/

    public void disposeImpl() {
    }

    public View getMainView() {
        return this.mainView;
    }

    /**************************** INHERITED METHODS *****************************/

    @Override
    public void onReceive(final Context context, Intent intent) {

        final String action = intent.getAction();
        if (action == null)
            return;

        if (action.equals(SHOW_PLUGIN)) {

            Log.d(TAG, "showing plugin drop down");
            showDropDown(mainView, HALF_WIDTH, FULL_HEIGHT, FULL_WIDTH,
                    HALF_HEIGHT, false, this);

            submit = mainView.findViewById(R.id.button1);
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    coords = getMainView().findViewById(R.id.coords);
                    String L = String.valueOf(coords.getText());
                    if (L.isEmpty()) {
                        Toast.makeText(context, "No coordinates", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (!L.contains(",")) {
                        Toast.makeText(context, "Bad format, try LAT,LNG,ID", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    double lat,lng;
                    String name;
                    try {
                        lat = Double.parseDouble(L.split(",")[0]);
                        lng = Double.parseDouble(L.split(",")[1]);
                        name = L.split(",")[2];
                    } catch (Exception e) {
                        Toast.makeText(context, "Bad format, try LAT,LNG,ID", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                        return;
                    }

                    String F = null;
                    type = getMainView().findViewById(R.id.Friend);
                    if (type.isChecked()) {
                        F = "a-f-G";
                    }
                    type = getMainView().findViewById(R.id.Hostile);
                    if (type.isChecked()) {
                        F = "a-h-G";
                    }
                    type = getMainView().findViewById(R.id.Neutral);
                    if (type.isChecked()) {
                        F = "a-n-G";
                    }
                    type = getMainView().findViewById(R.id.Unknown);
                    if (type.isChecked()) {
                        F = "a-u-G";
                    }

                    CotEvent cotEvent = new CotEvent();
                    cotEvent.setPoint(new CotPoint(lat, lng, 1, 0,0));
                    CoordinatedTime time = new CoordinatedTime();
                    cotEvent.setTime(time);
                    cotEvent.setStart(time);
                    cotEvent.setStale(time.addDays(1));
                    cotEvent.setUID(name);
                    cotEvent.setType(F);
                    cotEvent.setHow("m-g");

                    CotDetail cotContact = new CotDetail("contact");
                    cotContact.setAttribute("callsign", String.format(Locale.US, "%s", name));

                    CotDetail cotDetail = new CotDetail("detail");
                    cotDetail.addChild(cotContact);
                    cotEvent.setDetail(cotDetail);

                    if (cotEvent.isValid()) {
                        CotMapComponent.getInternalDispatcher().dispatch(cotEvent);
                        CotMapComponent.getExternalDispatcher().dispatch(cotEvent);
                        Toast.makeText(context, "Creating CoT", Toast.LENGTH_SHORT).show();
                        coords.setText("");
                    }
                }
            });
        }
    }

    @Override
    public void onDropDownSelectionRemoved() {
    }

    @Override
    public void onDropDownVisible(boolean v) {
    }

    @Override
    public void onDropDownSizeChanged(double width, double height) {
    }

    @Override
    public void onDropDownClose() {
    }

}
