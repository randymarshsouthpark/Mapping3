package com.example.a2cricg55.mapping2;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.views.MapView;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.config.Configuration;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends Activity
{

    MapView mv;
    ItemizedIconOverlay<OverlayItem> items;
    ItemizedIconOverlay.OnItemGestureListener<OverlayItem> markerGestureListener;

    public void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // this line tells OpenStreetMap about our app.
        // If you miss this out, you might get banned from OSM servers
        Configuration.getInstance().load
                (this, PreferenceManager.getDefaultSharedPreferences(this));

        mv = (MapView)findViewById(R.id.map1);

        mv.setBuiltInZoomControls(true);
        mv.getController().setZoom(14);
        mv.getController().setCenter(new GeoPoint(51.8833,-0.4167));

        markerGestureListener = new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>()
        {
            public boolean onItemLongPress(int i, OverlayItem item)
            {
                Toast.makeText(MainActivity.this, item.getSnippet(), Toast.LENGTH_SHORT).show();
                return true;
            }

            public boolean onItemSingleTapUp(int i, OverlayItem item)
            {
                Toast.makeText(MainActivity.this, item.getSnippet(), Toast.LENGTH_SHORT).show();
                return true;
            }
        };

        items = new ItemizedIconOverlay<OverlayItem>(this, new ArrayList<OverlayItem>(), markerGestureListener);
        OverlayItem Luton = new OverlayItem("Luton", "the town of the hatters", new GeoPoint(51.8833,-0.4167));
       // Luton.setMarker(getResources().getDrawable(R.drawable.marker));
        items.addItem(Luton);
        items.addItem(new OverlayItem("Dunstable", "Luton's evil twin", new GeoPoint(51.8833, -0.5167)));
        mv.getOverlays().add(items);

        try{
            BufferedReader newReader = new BufferedReader(new FileReader(Environment.getExternalStorageDirectory().getAbsolutePath()+"/poi.txt"));
            String newLine;
            while ((newLine = newReader.readLine()) !=null) {
                String[] components = newLine.split(",");
                if (components.length == 5) {
                    OverlayItem currentItem = new OverlayItem (components[0], components[2], new GeoPoint(Double.parseDouble(components[4]), Double.parseDouble(components[3])));
                    items.addItem(currentItem);

                }

            }
        }
        catch (IOException e){
            new AlertDialog.Builder(this).setMessage("ERROR: " + e).show();
        }
    }


    protected void onActivityResult(int requestCode,int resultCode,Intent intent)
    {

        if(resultCode==RESULT_OK)
        {
            if (requestCode==0)
            {
                Bundle extras=intent.getExtras();
                boolean cyclemap = extras.getBoolean("com.example.cyclemap");
                if(cyclemap==true)
                {
                    mv.setTileSource(TileSourceFactory.CYCLEMAP);
                }
                else
                {
                    mv.setTileSource(TileSourceFactory.MAPNIK);
                }
            }
            else if(requestCode==1)
            {
                Bundle extras=intent.getExtras();
                double latitude = extras.getDouble("com.example.thelocationlat");
                double longitude = extras.getDouble("com.example.thelocationlong");
                mv.getController().setCenter(new GeoPoint(latitude,longitude));

            }
        }
    }


    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.map_hello_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId() == R.id.choosemap)
        {
            // react to the menu item being selected...
            Intent intent = new Intent(this,MapChooseActivity.class);
            startActivityForResult(intent,0);
            return true;
        }
        else if(item.getItemId() == R.id.listofpoi)
        {
            Intent intent   = new Intent(this,ExampleListActivity.class);
            startActivityForResult(intent,2);
            return true;
        }
        else if(item.getItemId() == R.id.longlat)
        {
            Intent intent   = new Intent(this,locationactivity.class);
            startActivityForResult(intent,1);
            return true;
        }
        return false;
    }
}