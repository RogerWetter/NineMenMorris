package ch.zhaw.ninemenmorris

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import ch.zhaw.ninemenmorris.databinding.FragmentGameBoardBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class GameBoardFragment : Fragment() {

    private lateinit var mainActivity: MainActivity
    private var _binding: FragmentGameBoardBinding? = null
    private var gameLogic: GameLogic = GameLogic()
    private val pointIDArray = IntArray(24)

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        _binding = FragmentGameBoardBinding.inflate(inflater, container, false)
        mainActivity = activity as MainActivity
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setPlayer()
        setPointArray()
        setCoinStock()

        binding.point0.setOnClickListener {
            drawNewBoard(gameLogic.pointClicked(0))
        }

        binding.point1.setOnClickListener {
            drawNewBoard(gameLogic.pointClicked(1))
        }

        binding.point2.setOnClickListener {
            drawNewBoard(gameLogic.pointClicked(2))
        }

        binding.point3.setOnClickListener {
            drawNewBoard(gameLogic.pointClicked(3))
        }

        binding.point4.setOnClickListener {
            drawNewBoard(gameLogic.pointClicked(4))
        }

        binding.point5.setOnClickListener {
            drawNewBoard(gameLogic.pointClicked(5))
        }

        binding.point6.setOnClickListener {
            drawNewBoard(gameLogic.pointClicked(6))
        }

        binding.point7.setOnClickListener {
            drawNewBoard(gameLogic.pointClicked(7))
        }

        binding.point8.setOnClickListener {
            drawNewBoard(gameLogic.pointClicked(8))
        }

        binding.point9.setOnClickListener {
            drawNewBoard(gameLogic.pointClicked(9))
        }

        binding.point10.setOnClickListener {
            drawNewBoard(gameLogic.pointClicked(10))
        }

        binding.point11.setOnClickListener {
            drawNewBoard(gameLogic.pointClicked(11))
        }

        binding.point12.setOnClickListener {
            drawNewBoard(gameLogic.pointClicked(12))
        }

        binding.point13.setOnClickListener {
            drawNewBoard(gameLogic.pointClicked(13))
        }

        binding.point14.setOnClickListener {
            drawNewBoard(gameLogic.pointClicked(14))
        }

        binding.point15.setOnClickListener {
            drawNewBoard(gameLogic.pointClicked(15))
        }

        binding.point16.setOnClickListener {
            drawNewBoard(gameLogic.pointClicked(16))
        }

        binding.point17.setOnClickListener {
            drawNewBoard(gameLogic.pointClicked(17))
        }

        binding.point18.setOnClickListener {
            drawNewBoard(gameLogic.pointClicked(18))
        }

        binding.point19.setOnClickListener {
            drawNewBoard(gameLogic.pointClicked(19))
        }

        binding.point20.setOnClickListener {
            drawNewBoard(gameLogic.pointClicked(20))
        }

        binding.point21.setOnClickListener {
            drawNewBoard(gameLogic.pointClicked(21))
        }

        binding.point22.setOnClickListener {
            drawNewBoard(gameLogic.pointClicked(22))
        }

        binding.point23.setOnClickListener {
            drawNewBoard(gameLogic.pointClicked(23))
        }
    }

    private fun setCoinStock() {
        binding.countWhiteCoins.text = gameLogic.getWhiteCoinsStock().toString()
        binding.countBlackCoins.text = gameLogic.getBlackCoinsStock().toString()
    }

    private fun setPointArray() {
        pointIDArray[0] = R.id.point0
        pointIDArray[1] = R.id.point1
        pointIDArray[2] = R.id.point2
        pointIDArray[3] = R.id.point3
        pointIDArray[4] = R.id.point4
        pointIDArray[5] = R.id.point5
        pointIDArray[6] = R.id.point6
        pointIDArray[7] = R.id.point7
        pointIDArray[8] = R.id.point8
        pointIDArray[9] = R.id.point9
        pointIDArray[10] = R.id.point10
        pointIDArray[11] = R.id.point11
        pointIDArray[12] = R.id.point12
        pointIDArray[13] = R.id.point13
        pointIDArray[14] = R.id.point14
        pointIDArray[15] = R.id.point15
        pointIDArray[16] = R.id.point16
        pointIDArray[17] = R.id.point17
        pointIDArray[18] = R.id.point18
        pointIDArray[19] = R.id.point19
        pointIDArray[20] = R.id.point20
        pointIDArray[21] = R.id.point21
        pointIDArray[22] = R.id.point22
        pointIDArray[23] = R.id.point23
    }

    private fun drawBoard(field: Array<GameLogic.PointState>) {
        val length = field.size - 1
        for (i in 0..length) {
            when (field[i]) {
                GameLogic.PointState.Free -> {
                    mainActivity.findViewById<ImageButton>(pointIDArray[i])
                        .setImageResource(R.drawable.corner_point_black)
                }
                GameLogic.PointState.White -> {
                    mainActivity.findViewById<ImageButton>(pointIDArray[i])
                        .setImageResource(R.drawable.white_stone)
                }
                GameLogic.PointState.WhiteMarked -> {
                    mainActivity.findViewById<ImageButton>(pointIDArray[i])
                        .setImageResource(R.drawable.white_stone_marked)
                }
                GameLogic.PointState.Black -> {
                    mainActivity.findViewById<ImageButton>(pointIDArray[i])
                        .setImageResource(R.drawable.black_stone)
                }
                GameLogic.PointState.BlackMarked -> {
                    mainActivity.findViewById<ImageButton>(pointIDArray[i])
                        .setImageResource(R.drawable.black_stone_marked)
                }
                GameLogic.PointState.Marked -> {
                    mainActivity.findViewById<ImageButton>(pointIDArray[i])
                        .setImageResource(R.drawable.corner_point_orange)
                }
            }
        }
    }

    private fun resetBoard() {
        for (i in 0..23) {
            mainActivity.findViewById<ImageButton>(pointIDArray[i])
                .setImageResource(R.drawable.corner_point_black)
        }
    }

    private fun drawNewBoard(field: Array<GameLogic.PointState>) {
        drawBoard(field)
        if (gameLogic.getWin()) {
            val builder = AlertDialog.Builder(mainActivity)
            builder.setTitle(getString(R.string.win, getActivePlayerName()))

            builder.setPositiveButton("Play Again"){ _, _ ->
                gameLogic = GameLogic()
                resetBoard()
                setPlayer()
                setCoinStock()
            }
            builder.setNeutralButton("back to Game"){ _, _ ->
                /*Do nothing*/
            }
            val alertDialog: AlertDialog = builder.create()
            alertDialog.setCancelable(false)
            alertDialog.show()
        }
        setCoinStock()
        setPlayer()
    }

    private fun setPlayer() {
        binding.gameText.text =
            getString(R.string.player_x_turn, getActivePlayerName(), gameLogic.getActivePlayer())
    }

    private fun getActivePlayerName(): String {
        return if (gameLogic.getActivePlayer() == GameLogic.Player.White) {
            mainActivity.whitePlayerName
        } else {
            mainActivity.blackPlayerName
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
