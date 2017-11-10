package com.example.renwuhulianwang.myapplication.job;

import android.content.Context;

import com.example.renwuhulianwang.myapplication.Config;
import com.example.renwuhulianwang.myapplication.service.LuckyMoneyService;


public abstract class BaseAccessbilityJob implements AccessbilityJob {

    private LuckyMoneyService service;

    @Override
    public void onCreateJob(LuckyMoneyService service) {
        this.service = service;
    }

    public Context getContext() {
        return service.getApplicationContext();
    }

    public Config getConfig() {
        return service.getConfig();
    }

    public LuckyMoneyService getService() {
        return service;
    }
}
