package com.machiav3lli.discus.client.ui.fragments

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.machiav3lli.discus.client.R
import com.machiav3lli.discus.client.WRITE_PERMISSION
import com.machiav3lli.discus.client.databinding.MainFragmentBinding
import com.machiav3lli.discus.client.ui.activities.MainActivity
import com.machiav3lli.discus.client.ui.items.ProjectItem
import com.machiav3lli.discus.client.ui.viewmodels.MainViewModel
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.IAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var binding: MainFragmentBinding
    private val viewModel: MainViewModel by viewModels {
        MainViewModel.Factory((requireActivity() as MainActivity).db)
    }
    private val projectsItemAdapter = ItemAdapter<ProjectItem>()
    private var projectsFastAdapter: FastAdapter<ProjectItem>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MainFragmentBinding.inflate(inflater, container, false)
        binding.mainViewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        projectsFastAdapter = FastAdapter.with(projectsItemAdapter)
        projectsFastAdapter?.setHasStableIds(true)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = projectsFastAdapter
        projectsFastAdapter?.onClickListener =
            { _: View?, _: IAdapter<ProjectItem>?, item: ProjectItem?, _: Int? ->
                item?.project?.id?.let {
                    ProjectSheet(it).showNow(
                        parentFragmentManager,
                        "Project $it"
                    )
                }
                false
            }


        viewModel.projects.observe(viewLifecycleOwner) {
            projectsItemAdapter.set(it.map { item -> ProjectItem(item) })
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == WRITE_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.w(
                    this::class.java.name,
                    "Permissions were granted: $permissions -> $grantResults"
                )
            } else {
                Log.w(
                    this::class.java.name,
                    "Permissions were not granted: $permissions -> $grantResults"
                )
                Toast.makeText(
                    requireContext(),
                    getString(R.string.permission_not_granted),
                    Toast.LENGTH_LONG
                ).show()
            }
        } else {
            Log.w(this::class.java.name, "Unknown permissions request code: $requestCode")
        }
    }
}
