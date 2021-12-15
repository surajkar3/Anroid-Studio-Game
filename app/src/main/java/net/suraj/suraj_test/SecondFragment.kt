package net.suraj.suraj_test

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import net.suraj.suraj_test.databinding.FragmentSecondBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonSecond.setOnClickListener {
            binding.customView.buyTower();
        }
        binding.buttonSecond2.setOnClickListener {
            binding.customView.placeEnemy();
        }
        binding.buttonSecond3.setOnClickListener {
            binding.customView.moveMode();
        }
        binding.buttonSecond4.setOnClickListener {
            binding.buttonSecond4.visibility = View.INVISIBLE;
            binding.customView.startCombat();
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}