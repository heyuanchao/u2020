package com.jakewharton.u2020;

import android.app.Application;
import android.content.Context;
import com.jakewharton.u2020.data.LumberYard;
import com.jakewharton.u2020.ui.ActivityHierarchyServer;
import dagger.ObjectGraph;
import javax.inject.Inject;
import net.danlew.android.joda.JodaTimeAndroid;
import timber.log.Timber;

import static timber.log.Timber.DebugTree;

public final class U2020App extends Application {
  private ObjectGraph objectGraph;

  @Inject ActivityHierarchyServer activityHierarchyServer;
  @Inject LumberYard lumberYard;

  @Override public void onCreate() {
    super.onCreate();
    JodaTimeAndroid.init(this);

    if (BuildConfig.DEBUG) {
      Timber.plant(new DebugTree());
    } else {
      // TODO Crashlytics.start(this);
      // TODO Timber.plant(new CrashlyticsTree());
    }

    buildObjectGraphAndInject();

    lumberYard.cleanUp();
    Timber.plant(lumberYard.tree());

    registerActivityLifecycleCallbacks(activityHierarchyServer);
  }

  public void buildObjectGraphAndInject() {
    objectGraph = ObjectGraph.create(Modules.list(this));
    objectGraph.inject(this);
  }

  public void inject(Object o) {
    objectGraph.inject(o);
  }

  public static U2020App get(Context context) {
    return (U2020App) context.getApplicationContext();
  }
}
