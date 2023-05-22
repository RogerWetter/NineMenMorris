package ch.zhaw.ninemenmorris

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ch.zhaw.ninemenmorris.databinding.FragmentFirstBinding
import kotlinx.android.synthetic.main.fragment_first.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class StartFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFirst.setOnClickListener {

            val mainActivity: MainActivity? = activity as MainActivity?
            if (mainActivity != null) {
                mainActivity.whitePlayerName = if (textWhiteCoins.text.isNotBlank()) {
                    textWhiteCoins.text.toString()
                } else {
                    getString(R.string.name_white)
                }
                mainActivity.blackPlayerName = if (textBlackCoins.text.isNotBlank()) {
                    textBlackCoins.text.toString()
                } else {
                    getString(R.string.name_black)
                }
            }

            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
