package com.machiav3lli.discus.client.ui.items

import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import com.machiav3lli.discus.client.R
import com.machiav3lli.discus.client.data.Project
import com.machiav3lli.discus.client.databinding.ItemProjectBinding
import com.mikepenz.fastadapter.binding.AbstractBindingItem

class ProjectItem(var project: Project) : AbstractBindingItem<ItemProjectBinding>() {

    override val type: Int
        get() = R.id.fastadapter_item

    override var identifier: Long
        get() = project.hashCode().toLong()
        set(value) {
            super.identifier = value
        }

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): ItemProjectBinding =
        ItemProjectBinding.inflate(inflater, parent, false)

    override fun bindView(binding: ItemProjectBinding, payloads: List<Any>) {
        super.bindView(binding, payloads)
        binding.question.text = project.question
        binding.date.text =
            DateFormat.getLongDateFormat(binding.root.context).format(project.meetingDateTime.time)
    }

    override fun unbindView(binding: ItemProjectBinding) {
        super.unbindView(binding)
        binding.question.text = null
        binding.date.text = null
    }
}
