package com.lyft.cityguide.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * @class BaseAdapter
 * @brief Generic base adapter
 */
public abstract class BaseAdapter<T> extends android.widget.BaseAdapter {
    private List<T>        _items;
    private Context        _context;
    private LayoutInflater _inflater;

    public BaseAdapter(List<T> items, Context context) {
        this._items = items;
        this._context = context;
        this._inflater = LayoutInflater.from(this._context);
    }

    @Override
    public int getCount() {
        return _items.size();
    }

    @Override
    public Object getItem(int position) {
        return _items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public List<T> getItems() {
        return _items;
    }

    public T itemAt(int position) {
        return _items.get(position);
    }

    public Context getContext() {
        return _context;
    }

    public LayoutInflater getLayoutInflater() {
        return _inflater;
    }

    /**
     * Recycles view if possible, else instanciante a new one
     *
     * @param convertView
     * @param layoutId
     * @param parent
     * @param <U>
     * @return
     */
    public <U extends View> U recycle(View convertView, int layoutId, ViewGroup parent) {
        if (convertView == null) {
            return (U) getLayoutInflater().inflate(layoutId, parent, false);
        } else {
            return (U) convertView;
        }
    }

    public void appendItems(List<T> items) {
        for (T i : items) {
            _items.add(i);
        }
    }
}
