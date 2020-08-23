//package com.codingwithmitch.openapi.util
//
//import android.view.ViewGroup
//import androidx.databinding.ViewDataBinding
//import androidx.recyclerview.widget.AsyncDifferConfig
//import androidx.recyclerview.widget.DiffUtil
//import androidx.recyclerview.widget.RecyclerView
//
//abstract class GenericListAdapter<T>(
//) : RecyclerView.Adapter<GenericViewHolder<>>(
//) {
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBoundViewHolder {
//        val binding = createBinding(parent, viewType)
//        val viewHolder = DataBoundViewHolder(binding)
//        return viewHolder
//    }
//
//    protected abstract fun createBinding(parent: ViewGroup, viewType: Int): ViewDataBinding
//
//
//}