package com.mxt.anitrend.presenter.base;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import com.annimon.stream.Stream;
import com.mxt.anitrend.base.custom.presenter.CommonPresenter;
import com.mxt.anitrend.model.entity.anilist.UserStats;
import com.mxt.anitrend.model.entity.anilist.meta.GenreStats;
import com.mxt.anitrend.model.entity.base.UserBase;
import com.mxt.anitrend.model.entity.crunchy.MediaContent;
import com.mxt.anitrend.model.entity.crunchy.Thumbnail;
import com.mxt.anitrend.service.TagGenreService;
import com.mxt.anitrend.util.CompatUtil;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by max on 2017/09/16.
 * General presenter for most objects
 */

public class BasePresenter extends CommonPresenter {

    private List<String> favouriteGenres;

    public BasePresenter(Context context) {
        super(context);
    }

    public final void checkGenresAndTags(FragmentActivity fragmentActivity) {
        Intent intent = new Intent(fragmentActivity, TagGenreService.class);
        fragmentActivity.startService(intent);
    }

    public String getThumbnail(List<Thumbnail> thumbnails) {
        if(CompatUtil.isEmpty(thumbnails))
            return null;
        return thumbnails.get(0).getUrl();
    }

    public String getDuration(MediaContent mediaContent) {
        if(mediaContent.getDuration() != null) {
            long timeSpan = Integer.valueOf(mediaContent.getDuration());
            long minutes = TimeUnit.SECONDS.toMinutes(timeSpan);
            long seconds = timeSpan - TimeUnit.MINUTES.toSeconds(minutes);
            return (String.format(Locale.getDefault(), seconds < 10 ? "%d:0%d":"%d:%d", minutes, seconds));
        }
        return "00:00";
    }

    public List<String> getTopFavouriteGenres() {
        if(CompatUtil.isEmpty(favouriteGenres)) {
            UserStats userStats;
            if (getDatabase().getCurrentUser() != null && (userStats = getDatabase().getCurrentUser().getStats()) != null) {
                if (!CompatUtil.isEmpty(userStats.getFavouredGenresOverview())) {
                    favouriteGenres = Stream.of(userStats.getFavouredGenresOverview())
                            .sortBy(genreStat -> - genreStat.getAmount())
                            .map(GenreStats::getGenre)
                            .limit(4).toList();

                }
            }
        }
        return favouriteGenres;
    }

    public boolean isCurrentUser(long userId) {
        return getApplicationPref().isAuthenticated() && getDatabase().getCurrentUser() != null &&
                userId != 0 && getDatabase().getCurrentUser().getId() == userId;
    }

    public boolean isCurrentUser(String userName) {
        return getApplicationPref().isAuthenticated() && getDatabase().getCurrentUser() != null &&
                userName != null && getDatabase().getCurrentUser().getName().equals(userName);
    }

    public boolean isCurrentUser(long userId, String userName) {
        if (userName != null)
            return isCurrentUser(userName);
        return isCurrentUser(userId);
    }

    public boolean isCurrentUser(UserBase userBase) {
        return userBase != null && isCurrentUser(userBase.getId());
    }
}
