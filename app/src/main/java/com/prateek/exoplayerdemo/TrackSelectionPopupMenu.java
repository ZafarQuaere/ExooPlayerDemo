package com.prateek.exoplayerdemo;

import android.content.Context;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.appcompat.widget.PopupMenu;
import androidx.media3.common.TrackGroup;
import androidx.media3.common.TrackSelectionParameters;
import androidx.media3.common.Tracks;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.exoplayer.trackselection.TrackSelection;

import com.google.common.collect.ImmutableList;

import java.util.List;

public class TrackSelectionPopupMenu extends PopupMenu {
    private final ExoPlayer player;
    private final TrackSelectionParameters parameters;

    public TrackSelectionPopupMenu(Context context, ImageView imageView, ExoPlayer player, TrackSelectionParameters parameters) {
        super(context,imageView);
        this.player = player;
        this.parameters = parameters;
    }

    @Override
    public void show() {
        // Get the track groups and track selections from the player.
        ImmutableList<Tracks.Group> trackGroups = player.getCurrentTracks().getGroups();
//        List<TrackSelection> trackSelections = player.getTrackSelector().getCurrentTrackSelections();

        // Create a menu item for each track.
        for (int i = 0; i < trackGroups.size(); i++) {
//            TrackGroup trackGroup = trackGroups.get(i);
//            TrackSelection trackSelection = trackSelections.get(i);
//
//             Add the track to the menu.
//            this.getMenu().add(trackGroup.getFormat(0).language, trackSelection);
        }
        super.show();
    }

    /*@Override
    public boolean onMenuItemClick(MenuItem item) {
        // Get the track selection associated with the item.
        TrackSelection trackSelection = (TrackSelection) item.getTag();

        // Set the track selection on the player.
        player.setTrackSelectionParameters(parameters.Builder().setOverrideForType(trackSelection, 0).build());

        return true;
    }*/
}
