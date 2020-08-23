package com.codingwithmitch.openapi.ui.main.blog

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.*
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.codingwithmitch.openapi.R
import com.codingwithmitch.openapi.databinding.LayoutBlogListItemBinding
import com.codingwithmitch.openapi.databinding.LayoutNoMoreResultsBinding
import com.codingwithmitch.openapi.models.BlogPost
import com.codingwithmitch.openapi.util.DateUtils
import com.codingwithmitch.openapi.util.GenericViewHolder

class BlogListAdapter(
    private val interaction: Interaction? = null,
    private val requestManager: RequestManager,
    private val lifeCycleOwner: LifecycleOwner
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TAG = "AppDebug"
    private val NO_MORE_RESULTS = -1
    private val BLOG_ITEM = 0
    private val NO_MORE_RESULTS_BLOG_MARKER = BlogPost(
        NO_MORE_RESULTS,
        "",
        "",
        "",
        "",
        0L,
        ""
    )

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<BlogPost>() {

        override fun areItemsTheSame(oldItem: BlogPost, newItem: BlogPost): Boolean {
            return oldItem.pk == newItem.pk
        }

        override fun areContentsTheSame(oldItem: BlogPost, newItem: BlogPost): Boolean {
            return oldItem == newItem
        }

    }


    private val differ = AsyncListDiffer(
        BlogRecyclerChangeCallback(this),
        AsyncDifferConfig.Builder(DIFF_CALLBACK).build()
    )


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        Log.d(TAG, "OnCreateViewHolder : Case : $viewType")
        val layoutInflater = LayoutInflater.from(parent.context)
        when (viewType) {
            NO_MORE_RESULTS -> {
                val binding: LayoutNoMoreResultsBinding =
                    DataBindingUtil.inflate(
                        layoutInflater, R.layout.layout_no_more_results,
                        parent,
                        false
                    )
                binding.lifecycleOwner = lifeCycleOwner
                return GenericViewHolder(binding.root)
            }
            else -> {
                Log.d(TAG, "OnCreatViewHolder : Case Blog Ada")
                val binding: LayoutBlogListItemBinding =
                    DataBindingUtil.inflate(
                        layoutInflater, R.layout.layout_blog_list_item,
                        parent,
                        false
                    )
                binding.lifecycleOwner = lifeCycleOwner
                return BlogViewHolder(
                    binding,
                    requestManager,
                    interaction
                )
            }
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.d(TAG,"BlogListAdapter : OnBindingViewHolder")
        when (holder) {
            is BlogViewHolder -> {
                Log.d(TAG,"BlogListAdapter : OnBindingBlog ViewHolder $position")
                holder.bind(differ.currentList.get(position))
            }
        }
    }

    internal inner class BlogRecyclerChangeCallback(
        private val adapter: BlogListAdapter
    ) : ListUpdateCallback {
        override fun onChanged(position: Int, count: Int, payload: Any?) {
            adapter.notifyItemRangeChanged(position, count, payload)
        }

        override fun onInserted(position: Int, count: Int) {
            adapter.notifyItemRangeChanged(position, count)
        }

        override fun onMoved(fromPosition: Int, toPosition: Int) {
            adapter.notifyDataSetChanged()
        }

        override fun onRemoved(position: Int, count: Int) {
            adapter.notifyDataSetChanged()
        }

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun getItemViewType(position: Int): Int {
        if (differ.currentList.get(position).pk > -1) {
            return BLOG_ITEM
        }
        return differ.currentList.get(position).pk
    }

    fun submitList(list: List<BlogPost>?, isQueryExhausted: Boolean) {
        Log.d(TAG , "BlogListAdapter : NewItem : ${list?.size}")
        val newList = list?.toMutableList()
        if (isQueryExhausted) {
            newList?.add(NO_MORE_RESULTS_BLOG_MARKER)
        }
        differ.submitList(list)
    }

    class BlogViewHolder
    constructor(
        val binding: LayoutBlogListItemBinding,
        private val requestManager: RequestManager,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(binding.root) {
        val TAG = "AppDebug"
        fun bind(item: BlogPost) = with(binding) {
            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }
            Log.d(TAG,"Binding ViewHolder New Item ${item.title}")
            requestManager
                .load(item.image)
                .transition(withCrossFade())
                .into(blogImage)

            blogTitle.text = item.title
            blogAuthor.text = item.username
            blogUpdateDate.text = DateUtils.convertLongToStringDate(item.date_updated)
        }

    }

    interface Interaction {
        fun onItemSelected(position: Int, item: BlogPost)
    }


}
