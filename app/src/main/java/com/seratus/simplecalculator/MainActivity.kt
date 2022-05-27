package com.seratus.simplecalculator

import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.lifecycle.lifecycleScope
import com.seratus.simplecalculator.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private val viewModel by viewModels<MainViewModel>()

    companion object {
        private const val NUMBER_BUTTON = "NUMBER"
        private const val OPERATOR_BUTTON = "OPERATOR"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        subscribeCollector()
        val mButtons = binding.buttonContainer.children.filterIsInstance(Button::class.java)

        mButtons.filter {
            it.tag == NUMBER_BUTTON
        }.apply {
            this.forEach { button ->
                button.setOnClickListener {
                    viewModel.onButtonNumberClicked(button.text)
                }
            }
        }

        mButtons
            .filter { it.tag != null }
            .filter { (it.tag as String).contains(OPERATOR_BUTTON) }
            .apply {
                this.forEach { operatorButton ->
                    operatorButton.setOnClickListener {
                        val text = binding.resultText.text
                        viewModel.setValueToCalculate(text)
                        viewModel.onOperatorClicked(it.tag.toString())
                    }
                }
        }
    }

    private fun subscribeCollector(){
        lifecycleScope.launch {
            viewModel.operatorDisplayText.collectLatest {
                binding.calculationText.text = it
            }
        }

        lifecycleScope.launch {
            viewModel.textResult.collectLatest {
                binding.resultText.text = it
            }
        }
    }
}