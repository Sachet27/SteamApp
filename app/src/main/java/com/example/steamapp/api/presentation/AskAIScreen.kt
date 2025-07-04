package com.example.steamapp.api.presentation

import android.widget.Space
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ChipColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.steamapp.R
import com.example.steamapp.api.domain.models.AnswerStyle
import com.example.steamapp.quiz_feature.presentation.add_and_edit.components.QuizEditTopBar
import com.example.steamapp.ui.theme.SteamAppTheme
import com.example.steamapp.ui.theme.yellowish

@Composable
fun AskAIScreen(
    modifier: Modifier = Modifier,
    state: AIQuestionState,
    onAPIActions: (APIActions)->Unit,
    onBackNav: ()->Unit,
    userId: String
) {
    val context= LocalContext.current
    var question by remember{ mutableStateOf("") }
    var think by remember { mutableStateOf(false) }
    var micOn by remember{ mutableStateOf(false) }

    Scaffold(
       topBar = {
           QuizEditTopBar(
               title = "Ask AI",
               onSaveNote = onBackNav,
               onBackNav = onBackNav,
           )
       }
   ) {padding->
        if(state.question==null && state.answer==null){
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Image(
                    painter = painterResource(R.drawable.ask_ai_illustration),
                    contentDescription = "no quiz added yet",
                    modifier = Modifier.size(300.dp),
                    alpha = 0.8f
                )
                Text(
                    text = "Ask me a question!.",
                    color = MaterialTheme.colorScheme.secondary,
                    fontSize = 16.sp
                )
            }
        }
           Column(
               modifier = modifier
                   .fillMaxSize()
                   .padding(padding)
                   .padding(top= 12.dp, start = 12.dp, end= 12.dp, bottom= 90.dp)
           ) {
               Spacer(Modifier.height(30.dp))

               if(state.question!=null){
                   Row(
                       modifier = modifier.fillMaxWidth(),
                       horizontalArrangement = Arrangement.End,
                       verticalAlignment = Alignment.CenterVertically
                   ){
                       Box(
                           modifier = Modifier
                               .clip(RoundedCornerShape(15.dp))
                               .background(MaterialTheme.colorScheme.surfaceContainer)
                               .weight(1f, false),
                       ){
                           Text(
                               modifier = Modifier.padding(16.dp),
                               text = state.question,
                               style = MaterialTheme.typography.bodyMedium
                           )
                       }
                       Image(
                           painter = painterResource(R.drawable.default_pfp),
                           contentDescription = null,
                           modifier = Modifier.padding(4.dp).clip(CircleShape).size(30.dp)
                       )
                   }
               }
               Spacer(Modifier.height(20.dp))
               if(state.isLoading && state.answer==null){
                   Row (horizontalArrangement = Arrangement.Center){
                       CircularProgressIndicator()

                   }
               }
               if(state.answer!=null){
                   Row(
                       modifier = modifier.fillMaxWidth(0.9f),
                       verticalAlignment = Alignment.CenterVertically
                   ){
                       Image(
                           painter = painterResource(R.drawable.chatbot_bot_icon),
                           contentDescription = null,
                           modifier = Modifier.padding(4.dp).clip(CircleShape).size(34.dp)
                       )
                       Box(
                           modifier = Modifier
                               .clip(RoundedCornerShape(15.dp))
                               .background(MaterialTheme.colorScheme.surfaceContainer)
                               .weight(1f, false),
                       ){
                           Column(
                               modifier = Modifier.padding(6.dp).verticalScroll(rememberScrollState())
                           ) {
                               Text(
                                   modifier = Modifier.padding(16.dp),
                                   text = state.answer,
                                   style = MaterialTheme.typography.bodyMedium
                               )
                           }
                       }
                   }
               }

           }
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Column(
                modifier = modifier
            ){
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clickable {
                                think= !think
                            }
                            .padding(8.dp)
                            .clip(RoundedCornerShape(40))
                            .border(
                                border = BorderStroke(0.5.dp,
                                    if(think) yellowish else MaterialTheme.colorScheme.onSurface),
                                shape = RoundedCornerShape(40)
                            )
                            .padding(vertical = 4.dp, horizontal = 8.dp)


                    ){
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.lightbulb_24px),
                            contentDescription = null,
                            tint = if(think) yellowish else MaterialTheme.colorScheme.onSurface
                        )
                        AnimatedVisibility(
                            visible = think
                        ) {
                            Text(
                                text= "Reason",
                                color = yellowish,
                                style = MaterialTheme.typography.labelLarge
                            )
                        }

                    }


                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clickable {
                                micOn= !micOn
                                onAPIActions(APIActions.onPushMicAction(micOn = micOn, context = context))
                            }
                            .padding(vertical = 8.dp, horizontal = 4.dp)
                            .clip(RoundedCornerShape(40))
                            .border(
                                border = BorderStroke(0.5.dp,
                                    if(micOn) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.error) ,
                                shape = RoundedCornerShape(40)
                            )
                            .padding(vertical = 4.dp, horizontal = 8.dp)

                    ){
                        Icon(
                            imageVector = ImageVector.vectorResource(
                                if(micOn) R.drawable.mic_24px else R.drawable.mic_off_24px
                            ),
                            contentDescription = null,
                            tint = if(micOn) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.error
                        )

                    }

                }



                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    OutlinedTextField(
                        modifier = Modifier.weight(1f),
                        value = question,
                        onValueChange = { question = it },
                        placeholder = {
                            Text(
                                text = "Ask a question!"
                            )
                        }
                    )
                    IconButton(
                        onClick = {
                            onAPIActions(APIActions.onClearAIQuestionState)
                            onAPIActions(APIActions.onAskOllama(
                                userId = userId,
                                question = question,
                                think = think
                            ))
                        }
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.send_icon),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }

            }
        }
    }
}


@Preview
@Composable
private fun AskAIPreview() {
    SteamAppTheme {
        AskAIScreen(
            state = AIQuestionState(
                question = "What is the capital of France?".repeat(2),
                answer = "Sure! Here's a random paragraph:\n" +
                        "\n" +
                        "The sky was painted in hues of orange and pink, with streaks of gold splashed across the horizon as the sun began its descent. A gentle breeze carried the scent of blooming jasmine, intertwining with the distant melody of chirping crickets. Along the cobblestone path, a child chased a fluttering butterfly, their laughter echoing through the tranquil evening. Nearby, an old oak tree stood tall and unwavering, its branches swaying gracefully as if nodding to the rhythm of the wind. It was one of those moments where time seemed to pause, allowing nature's beauty to take center stage."
            ),
            onAPIActions = {},
            onBackNav = {},
            userId = "user1"
        )
    }
}