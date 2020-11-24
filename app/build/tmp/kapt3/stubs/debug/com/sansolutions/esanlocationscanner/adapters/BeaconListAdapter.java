package com.sansolutions.esanlocationscanner.adapters;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\n\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001BE\u0012\u000e\u0010\u0003\u001a\n\u0012\u0004\u0012\u00020\u0005\u0018\u00010\u0004\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0012\u0010\b\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\n0\t\u0012\u0012\u0010\u000b\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\n0\t\u00a2\u0006\u0002\u0010\fJ\b\u0010\u0014\u001a\u00020\u0015H\u0016J\u0018\u0010\u0016\u001a\u00020\n2\u0006\u0010\u0017\u001a\u00020\u00022\u0006\u0010\u0018\u001a\u00020\u0015H\u0016J\u0018\u0010\u0019\u001a\u00020\u00022\u0006\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u0015H\u0016R\u001d\u0010\b\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\n0\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u001d\u0010\u000b\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\n0\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u000eR\u0019\u0010\u0003\u001a\n\u0012\u0004\u0012\u00020\u0005\u0018\u00010\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013\u00a8\u0006\u001d"}, d2 = {"Lcom/sansolutions/esanlocationscanner/adapters/BeaconListAdapter;", "Landroidx/recyclerview/widget/RecyclerView$Adapter;", "Lcom/sansolutions/esanlocationscanner/adapters/ViewHolder;", "items", "", "Lcom/sansolutions/esanlocationscanner/db/LocationEntity;", "context", "Landroid/content/Context;", "clickListener", "Lkotlin/Function1;", "", "deleteClickListener", "(Ljava/util/List;Landroid/content/Context;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;)V", "getClickListener", "()Lkotlin/jvm/functions/Function1;", "getContext", "()Landroid/content/Context;", "getDeleteClickListener", "getItems", "()Ljava/util/List;", "getItemCount", "", "onBindViewHolder", "holder", "position", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "app_debug"})
public final class BeaconListAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<com.sansolutions.esanlocationscanner.adapters.ViewHolder> {
    @org.jetbrains.annotations.Nullable()
    private final java.util.List<com.sansolutions.esanlocationscanner.db.LocationEntity> items = null;
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.jvm.functions.Function1<com.sansolutions.esanlocationscanner.db.LocationEntity, kotlin.Unit> clickListener = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.jvm.functions.Function1<com.sansolutions.esanlocationscanner.db.LocationEntity, kotlin.Unit> deleteClickListener = null;
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public com.sansolutions.esanlocationscanner.adapters.ViewHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.ViewGroup parent, int viewType) {
        return null;
    }
    
    @java.lang.Override()
    public int getItemCount() {
        return 0;
    }
    
    @java.lang.Override()
    public void onBindViewHolder(@org.jetbrains.annotations.NotNull()
    com.sansolutions.esanlocationscanner.adapters.ViewHolder holder, int position) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.util.List<com.sansolutions.esanlocationscanner.db.LocationEntity> getItems() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final android.content.Context getContext() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlin.jvm.functions.Function1<com.sansolutions.esanlocationscanner.db.LocationEntity, kotlin.Unit> getClickListener() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlin.jvm.functions.Function1<com.sansolutions.esanlocationscanner.db.LocationEntity, kotlin.Unit> getDeleteClickListener() {
        return null;
    }
    
    public BeaconListAdapter(@org.jetbrains.annotations.Nullable()
    java.util.List<com.sansolutions.esanlocationscanner.db.LocationEntity> items, @org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.sansolutions.esanlocationscanner.db.LocationEntity, kotlin.Unit> clickListener, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.sansolutions.esanlocationscanner.db.LocationEntity, kotlin.Unit> deleteClickListener) {
        super();
    }
}