package com.example.steamapp.quiz_feature.presentation.home.components

import android.os.Build
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import com.example.steamapp.R
import com.example.steamapp.quiz_feature.domain.models.Quiz
import com.example.steamapp.ui.theme.SteamAppTheme
import java.time.Instant
import java.time.LocalDateTime

@OptIn(ExperimentalFoundationApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun QuizCard(
    modifier: Modifier = Modifier,
    quiz: Quiz,
    @DrawableRes icon: Int,
    onClick: (Long)-> Unit,
    onDelete: ()->Unit,
    onIconClick: ()-> Unit
) {
    val date= quiz.lastUpdatedAt.toDateString()
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
            .combinedClickable(
                onClick = {onClick(quiz.quizId)},
                onLongClick = onDelete
            ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {
            Row(
                modifier = modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    modifier = Modifier.weight(0.8f),
                    text = quiz.title,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 15.sp
                )
                IconButton(
                    modifier = Modifier.weight(0.2f).height(34.dp),
                    onClick = { onIconClick() }
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(icon),
                        contentDescription = "More options",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(25.dp)
                    )
                }
            }
            Spacer(Modifier.height(2.dp))
            Text(
                text = quiz.description ?: "No description",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Box(
                modifier= Modifier.fillMaxSize(),
                contentAlignment = Alignment.BottomStart
            ){
                Row(
                    modifier= Modifier.fillMaxWidth().padding(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Text(
                        text = "${quiz.questionCount} questions",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Text(
                        text = "Last updated: ${date}",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun QuizCardPreview() {
    SteamAppTheme {
        QuizCard(
            quiz = Quiz(
                quizId = 1,
                title = "Mathematics Quiz and Science QUiz",
                description = "Simple Quiz to test trigonometry".repeat(4),
                lastUpdatedAt = Instant.now(),
                questionCount = 8
            ),
            onClick = {},
            modifier = Modifier,
            onIconClick = {},
            icon = R.drawable.push_to_raspberry_pi_icon,
            onDelete = {}
        )
    }
}