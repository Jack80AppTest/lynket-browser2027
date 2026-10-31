package arun.com.chromer.shared.epxoy.model

import arun.com.chromer.R
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import dev.arunkumar.android.epoxy.model.KotlinEpoxyModelWithHolder
import dev.arunkumar.android.epoxy.model.KotlinHolder
import arun.com.chromer.databinding.LayoutFeedHeaderBinding

@EpoxyModelClass(layout = R.layout.layout_feed_header)
abstract class HeaderLayoutModel : KotlinEpoxyModelWithHolder<HeaderLayoutModel.ViewHolder>() {
    class ViewHolder : KotlinHolder() {
        private val views by lazy(LazyThreadSafetyMode.NONE) { LayoutFeedHeaderBinding.bind(containerView) }
        val header get() = views.header
    }

    @EpoxyAttribute
    lateinit var title: String

    override fun bind(holder: ViewHolder) {
        holder.header.text = title
    }
}