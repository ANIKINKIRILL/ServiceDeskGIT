package com.test.admin.servicedesk;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public abstract class RecyclerViewScrollListener extends RecyclerView.OnScrollListener {


        LinearLayoutManager layoutManager;

        public RecyclerViewScrollListener(LinearLayoutManager layoutManager) {
            this.layoutManager = layoutManager;
        }

        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount();
            int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

            if (!isLoading() && !isLastPage()) {
                if ((visibleItemCount + firstVisibleItemPosition) >=
                        totalItemCount && firstVisibleItemPosition >= 0) {
                    loadMoreItems();
                }
            }
        }

        protected abstract void loadMoreItems();
        public abstract int getTotalPageCount();
        public abstract boolean isLastPage();
        public abstract boolean isLoading();

}
