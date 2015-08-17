package com.lyft.cityguide.models.bll.interfaces;

/**
 * @class IBLL
 * @brief Common interface for all BLL classes
 */
public interface IBLL {
    /**
     * Aborts any background task
     */
    void cancelAllTasks();
}
