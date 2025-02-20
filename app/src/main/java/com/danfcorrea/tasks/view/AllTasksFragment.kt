package com.danfcorrea.tasks.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.danfcorrea.tasks.databinding.FragmentAllTasksBinding
import com.danfcorrea.tasks.service.listener.TaskListener
import com.danfcorrea.tasks.view.adapter.TaskAdapter
import com.danfcorrea.tasks.viewmodel.TaskListViewModel

class AllTasksFragment : Fragment() {

    private lateinit var viewModel: TaskListViewModel
    private var _binding: FragmentAllTasksBinding? = null
    private val binding get() = _binding!!

    private val adapter = TaskAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, b: Bundle?): View {
        viewModel = ViewModelProvider(this)[TaskListViewModel::class.java]
        _binding = FragmentAllTasksBinding.inflate(inflater, container, false)

        binding.recyclerAllTasks.layoutManager = LinearLayoutManager(context)
        binding.recyclerAllTasks.adapter = adapter

        val listener = object :TaskListener{
            override fun onListClick(id: Int) {
            }

            override fun onDeleteClick(id: Int) {
            }

            override fun onCompleteClick(id: Int) {
            }

            override fun onUndoClick(id: Int) {
            }
        }

        adapter.attachListener(listener)

        // Cria os observadores
        observe()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.listTasks()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observe() {
        viewModel.tasks.observe(viewLifecycleOwner){
            adapter.updateTasks(it)
        }
    }
}