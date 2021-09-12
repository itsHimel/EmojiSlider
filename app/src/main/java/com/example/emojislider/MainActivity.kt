package com.example.emojislider

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.emojislider.ui.theme.EmojiSliderTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EmojiSliderTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    color = MaterialTheme.colors.background,
                    modifier = Modifier.fillMaxSize()

                ) {

                    EmojiSliderDemo()

                }
            }
        }
    }
}

@Composable
fun EmojiSliderDemo() {

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
        ) {

            Box(
                contentAlignment = Alignment.Center,
            ) {
                EmojiSlider(
                    emoji = Emoji.loveFace,
                    modifier = Modifier.align(Alignment.Center),
                ) { progress ->
                    Log.d("onProgressChange:", progress.toString())
                }
            }
            Spacer(modifier = Modifier.height(20.dp))

            Box(
                contentAlignment = Alignment.Center,
            ) {
                EmojiSlider(
                    emoji = Emoji.heart,
                    modifier = Modifier.align(Alignment.Center),
                ) { progress ->
                    Log.d("onProgressChange:", progress.toString())
                }
            }
            Spacer(modifier = Modifier.height(20.dp))

            Box(
                contentAlignment = Alignment.Center,
            ) {
                EmojiSlider(
                    emoji = Emoji.smiley,
                    modifier = Modifier.align(Alignment.Center),
                ) { progress ->
                    Log.d("onProgressChange:", progress.toString())
                }
            }
        }
    }
}


@Composable
fun EmojiSlider(
    modifier: Modifier = Modifier,
    width: Dp = 220.dp,
    height: Dp = 80.dp,
    progressColor: Color = Color(0xFFE1306C),
    emoji: String = Emoji.heart,
    emojiSize: Float = 66f,
    progressWidth: Float = 18f,
    onSlide: (Float) -> Unit

) {

    var isPressed by remember { mutableStateOf(false) }
    var offsetX by remember { mutableStateOf(10f) }
    var progress by remember { mutableStateOf(0f) }

    Box(modifier = Modifier) {
        Card(
            shape = RoundedCornerShape(8.dp),
            backgroundColor = Color.White,
            modifier = Modifier
                .height(height)
                .width(width)
                .align(Alignment.Center)
                .draggable(
                    orientation = Orientation.Horizontal,
                    state = rememberDraggableState { delta ->
                        offsetX += delta
                    },
                    onDragStopped = { isPressed = false },
                    onDragStarted = { isPressed = true }
                )
        ) {
            Canvas(
                modifier = modifier
                    .fillMaxSize()
                    .padding(24.dp)
            ) {
                val canvasWidth = size.width
                val canvasHeight = size.height
                offsetX = offsetX.coerceIn(0f, canvasWidth)
                progress = (offsetX / canvasWidth) * 100
                onSlide(progress)

                drawLine(
                    start = Offset(x = 0f, y = canvasHeight / 2),
                    end = Offset(x = canvasWidth, y = canvasHeight / 2),
                    color = Color.LightGray,
                    strokeWidth = progressWidth,
                    cap = StrokeCap.Round
                )

                drawLine(
                    start = Offset(x = 0f, y = canvasHeight / 2),
                    end = Offset(x = offsetX, y = canvasHeight / 2),
                    color = progressColor,
                    strokeWidth = progressWidth,
                    cap = StrokeCap.Round
                )

                drawIntoCanvas { canvas ->
                    canvas.nativeCanvas.drawText(
                        emoji,
                        offsetX - 26f,
                        (canvasHeight / 2) + 16f,
                        Paint().asFrameworkPaint().apply {
                            textSize = emojiSize
                        }

                    )

                }

            }

        }
        if (isPressed) {
            Text(
                text = emoji,
                fontSize = progress.coerceIn(20f, 60f).sp,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .offset {
                        IntOffset(offsetX.toInt(), -200)
                    }
            )
        }
    }

}
