package com.machiav3lli.discus.client.ui.fragments

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.machiav3lli.discus.client.EXTRA_PROJECT_ID
import com.machiav3lli.discus.client.R
import com.machiav3lli.discus.client.data.Project
import com.machiav3lli.discus.client.databinding.SheetEditProjectBinding
import com.machiav3lli.discus.client.projectsDirectory
import com.machiav3lli.discus.client.ui.activities.MainActivity
import com.machiav3lli.discus.client.ui.dialogs.EditStringDialog
import com.machiav3lli.discus.client.ui.viewmodels.EditSheetViewModel
import java.io.File
import java.util.*

class ProjectSheet() : BottomSheetDialogFragment() {
    private lateinit var binding: SheetEditProjectBinding
    val viewModel: EditSheetViewModel by viewModels {
        EditSheetViewModel.Factory((requireActivity() as MainActivity).db, projectId)
    }

    constructor(projectId: Long = 0) : this() {
        arguments = Bundle().apply {
            putLong(EXTRA_PROJECT_ID, projectId)
        }
    }

    private val projectId: Long
        get() = requireArguments().getLong(EXTRA_PROJECT_ID)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val sheet = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        sheet.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        return sheet
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SheetEditProjectBinding.inflate(layoutInflater, container, false)
        viewModel.project.observe(viewLifecycleOwner) { updateLayout(it) }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupLayout()
    }

    private fun updateLayout(project: Project) {
        listOf(
            binding.question,
            binding.webpage,
            binding.positiveReply,
            binding.neutralReply,
            binding.negativeReply,
            binding.pro1,
            binding.pro2,
            binding.pro3,
            binding.contra1,
            binding.contra2,
            binding.contra3,
            binding.time,
            binding.date,
            binding.invitation
        ).forEach {
            it.apply {
                text = when (id) {
                    R.id.question -> project.question
                    R.id.webpage -> project.webpage
                    R.id.positiveReply -> project.replyPositive
                    R.id.neutralReply -> project.replyNeutral
                    R.id.negativeReply -> project.replyNegative
                    R.id.pro1 -> project.argumentsPositive.elementAtOrNull(0)
                    R.id.pro2 -> project.argumentsPositive.elementAtOrNull(1)
                    R.id.pro3 -> project.argumentsPositive.elementAtOrNull(2)
                    R.id.contra1 -> project.argumentsNegative.elementAtOrNull(0)
                    R.id.contra2 -> project.argumentsNegative.elementAtOrNull(1)
                    R.id.contra3 -> project.argumentsNegative.elementAtOrNull(2)
                    R.id.time -> DateFormat.getTimeFormat(context)
                        .format(project.meetingDateTime.time)
                    R.id.date -> DateFormat.getDateFormat(context)
                        .format(project.meetingDateTime.time)
                    R.id.invitation -> project.invitation
                    else -> null
                }
            }
        }

    }

    private fun setupLayout() {
        binding.delete.setOnClickListener {
            dismissAllowingStateLoss()
            viewModel.deleteProject()
        }
        binding.create.setOnClickListener { viewModel.createFile(requireContext()) }
        binding.questionBlock.setOnClickListener {
            EditStringDialog(
                getString(R.string.question),
                viewModel.project.value?.question ?: ""
            ) {
                viewModel.project.value?.apply {
                    question = it
                    viewModel.updateProject(this)
                }
            }.show(parentFragmentManager, "Project's Question")
        }
        binding.webpageBlock.setOnClickListener {
            EditStringDialog(getString(R.string.webpage), viewModel.project.value?.webpage ?: "") {
                viewModel.project.value?.apply {
                    webpage = it
                    viewModel.updateProject(this)
                }
            }.show(parentFragmentManager, "Project's Question")
        }
        binding.positiveReply.setOnClickListener {
            EditStringDialog(
                getString(R.string.positive_reply),
                viewModel.project.value?.replyPositive ?: ""
            ) {
                viewModel.project.value?.apply {
                    replyPositive = it
                    viewModel.updateProject(this)
                }
            }.show(parentFragmentManager, "Project's Question")
        }
        binding.neutralReply.setOnClickListener {
            EditStringDialog(
                getString(R.string.neutrale_reply),
                viewModel.project.value?.replyNeutral ?: ""
            ) {
                viewModel.project.value?.apply {
                    replyNeutral = it
                    viewModel.updateProject(this)
                }
            }.show(parentFragmentManager, "Project's Question")
        }
        binding.negativeReply.setOnClickListener {
            EditStringDialog(
                getString(R.string.negative_reply),
                viewModel.project.value?.replyNegative ?: ""
            ) {
                viewModel.project.value?.apply {
                    replyNegative = it
                    viewModel.updateProject(this)
                }
            }.show(parentFragmentManager, "Project's Question")
        }
        listOf(binding.pro1Title, binding.pro2Title, binding.pro3Title)
            .forEachIndexed { index, appCompatTextView ->
                appCompatTextView.text = getString(R.string.pro_argument, index + 1)
            }
        listOf(binding.pro1Block, binding.pro2Block, binding.pro3Block)
            .forEachIndexed { index, linearLayoutCompat ->
                linearLayoutCompat.setOnClickListener {
                    EditStringDialog(
                        getString(R.string.pro_argument, index + 1),
                        viewModel.project.value?.argumentsPositive?.elementAt(index) ?: ""
                    ) {
                        viewModel.project.value?.apply {
                            argumentsPositive[index] = it
                            viewModel.updateProject(this)
                        }
                    }.show(parentFragmentManager, "Project's Question")
                }
            }
        listOf(binding.contra1Title, binding.contra2Title, binding.contra3Title)
            .forEachIndexed { index, appCompatTextView ->
                appCompatTextView.text = getString(R.string.contra_argument, index + 1)
            }
        listOf(binding.contra1Block, binding.contra2Block, binding.contra3Block)
            .forEachIndexed { index, linearLayoutCompat ->
                linearLayoutCompat.setOnClickListener {
                    EditStringDialog(
                        getString(R.string.contra_argument, index + 1),
                        viewModel.project.value?.argumentsNegative?.elementAt(index) ?: ""
                    ) {
                        viewModel.project.value?.apply {
                            argumentsNegative[index] = it
                            viewModel.updateProject(this)
                        }
                    }.show(parentFragmentManager, "Project's Question")
                }
            }
        binding.invitationBlock.setOnClickListener {
            EditStringDialog(
                getString(R.string.invitation_text),
                viewModel.project.value?.invitation ?: ""
            ) {
                viewModel.project.value?.apply {
                    invitation = it
                    viewModel.updateProject(this)
                }
            }.show(parentFragmentManager, "Project's Question")
        }
        binding.timeBlock.setOnClickListener {
            TimePickerDialog(
                requireContext(),
                com.google.android.material.R.style.ThemeOverlay_Material3_MaterialTimePicker,
                { _, hourOfDay, minute ->
                    viewModel.project.value?.apply {
                        meetingDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        meetingDateTime.set(Calendar.MINUTE, minute)
                        viewModel.updateProject(this)
                    }
                },
                viewModel.project.value?.meetingDateTime?.get(Calendar.HOUR_OF_DAY) ?: 0,
                viewModel.project.value?.meetingDateTime?.get(Calendar.MINUTE) ?: 0,
                true
            ).show()
        }
        binding.dateBlock.setOnClickListener {
            DatePickerDialog(
                requireContext(),
                com.google.android.material.R.style.ThemeOverlay_Material3_MaterialCalendar,
                { _, year, month, day ->
                    viewModel.project.value?.apply {
                        meetingDateTime.set(Calendar.YEAR, year)
                        meetingDateTime.set(Calendar.MONTH, month)
                        meetingDateTime.set(Calendar.DAY_OF_MONTH, day)
                        viewModel.updateProject(this)
                    }
                },
                viewModel.project.value?.meetingDateTime?.get(Calendar.YEAR) ?: 0,
                viewModel.project.value?.meetingDateTime?.get(Calendar.MONTH) ?: 0,
                viewModel.project.value?.meetingDateTime?.get(Calendar.DAY_OF_MONTH) ?: 0
            ).show()
        }
    }

    fun writeProjectFile() {
        val projectFile = File(requireContext().projectsDirectory, "Project_$projectId")
        projectFile.writeText(viewModel.project.value?.toJSON() ?: "")
    }
}
