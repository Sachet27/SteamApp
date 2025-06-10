package com.example.steamapp.student.quiz.domain.models

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.steamapp.material_feature.domain.models.StudyMaterial
import com.example.steamapp.quiz_feature.data.local.entities.QuestionEntity
import com.example.steamapp.quiz_feature.data.local.entities.QuizEntity
import com.example.steamapp.quiz_feature.data.local.entities.relations.QuizWithQuestions
import java.time.Instant

object DummyQuizWithQuestions {
    @RequiresApi(Build.VERSION_CODES.O)
    val scienceQuizWithQuestions = QuizWithQuestions(
        quiz = QuizEntity(
            quizId = 1L,
            title = "Cell Structure and Function",
            description = "Test your understanding of the cell, its organelles, and their functions.",
            lastUpdatedAt = Instant.now(),
            questionCount = 5
        ),
        questions = listOf(
            QuestionEntity(
                id = 1L,
                title = "Which of the following is the powerhouse of the cell?",
                options = listOf("Nucleus", "Mitochondria", "Ribosome", "Golgi Apparatus"),
                correctOptionIndex = 1,
                imageRelativePath = null,
                audioRelativePath = null,
                quizId = 1L
            ),
            QuestionEntity(
                id = 2L,
                title = "Identify the organelle shown in the image.",
                options = listOf("Lysosome", "Chloroplast", "Endoplasmic Reticulum", "Golgi Apparatus"),
                correctOptionIndex = 1,
                imageRelativePath = "/dummy/chloroplast.jpg",
                audioRelativePath = null,
                quizId = 1L
            ),
            QuestionEntity(
                id = 3L,
                title = "What is the function of ribosomes in a cell?",
                options = listOf(
                    "Transporting materials",
                    "Generating energy",
                    "Producing proteins",
                    "Breaking down waste"
                ),
                correctOptionIndex = 2,
                imageRelativePath = null,
                audioRelativePath = null,
                quizId = 1L
            ),
            QuestionEntity(
                id = 4L,
                title = "Listen to the audio and answer: What process is being described?",
                options = listOf(
                    "Osmosis",
                    "Photosynthesis",
                    "Cell Division",
                    "Active Transport"
                ),
                correctOptionIndex = 1,
                imageRelativePath = null,
                audioRelativePath = "/dummy/photosynthesis.mp3",
                quizId = 1L
            ),
            QuestionEntity(
                id = 5L,
                title = "Which structure in an animal cell contains the genetic material?",
                options = listOf("Mitochondria", "Nucleus", "Cytoplasm", "Cell Membrane"),
                correctOptionIndex = 1,
                imageRelativePath = null,
                audioRelativePath = null,
                quizId = 1L
            )
        )
    )

    @RequiresApi(Build.VERSION_CODES.O)
    val mathsQuizWithQuestions = QuizWithQuestions(
        quiz = QuizEntity(
            quizId = 2L,
            title = "Geometry Basics",
            description = "Test your understanding of basic geometry concepts and properties.",
            lastUpdatedAt = Instant.now(),
            questionCount = 5
        ),
        questions = listOf(
            QuestionEntity(
                id = 1L,
                title = "What is the sum of all angles in a triangle?",
                options = listOf("90°", "180°", "360°", "270°"),
                correctOptionIndex = 1,
                imageRelativePath = null,
                audioRelativePath = null,
                quizId = 2L
            ),
            QuestionEntity(
                id = 2L,
                title = "Identify the type of triangle shown in the image.",
                options = listOf("Scalene", "Isosceles", "Equilateral", "Right-angled"),
                correctOptionIndex = 3,
                imageRelativePath = "/dummy/right_angled_triangle.jpeg", // Replace with actual path
                audioRelativePath = null,
                quizId = 2L
            ),
            QuestionEntity(
                id = 3L,
                title = "What is the value of π (pi) approximately?",
                options = listOf("3.1", "3.14", "3.1415", "3.14159"),
                correctOptionIndex = 3,
                imageRelativePath = null,
                audioRelativePath = null,
                quizId = 2L
            ),
            QuestionEntity(
                id = 4L,
                title = "What shape is described as having all points equidistant from its center?",
                options = listOf("Circle", "Square", "Rectangle", "Triangle"),
                correctOptionIndex = 0,
                imageRelativePath = null,
                audioRelativePath = null,
                quizId = 2L
            ),
            QuestionEntity(
                id = 5L,
                title = "If a square has a side length of 5 cm, what is its area?",
                options = listOf("10 cm²", "20 cm²", "25 cm²", "30 cm²"),
                correctOptionIndex = 2,
                imageRelativePath = null,
                audioRelativePath = null,
                quizId = 2L
            )
        )
    )

    @RequiresApi(Build.VERSION_CODES.O)
    val englishQuizWithQuestions = QuizWithQuestions(
        quiz = QuizEntity(
            quizId = 3L,
            title = "Figures of Speech",
            description = "Test your understanding of different figures of speech in English literature.",
            lastUpdatedAt = Instant.now(),
            questionCount = 5
        ),
        questions = listOf(
            QuestionEntity(
                id = 1L,
                title = "Which figure of speech involves the repetition of initial consonant sounds?",
                options = listOf("Metaphor", "Alliteration", "Simile", "Hyperbole"),
                correctOptionIndex = 1,
                imageRelativePath = null,
                audioRelativePath = null,
                quizId = 3L
            ),
            QuestionEntity(
                id = 2L,
                title = "Identify the figure of speech illustrated in the image.",
                options = listOf("Personification", "Metaphor", "Simile", "Hyperbole"),
                correctOptionIndex = 0,
                imageRelativePath = "/dummy/personification.jpg",
                audioRelativePath = null,
                quizId = 3L
            ),
            QuestionEntity(
                id = 3L,
                title = "What is the figure of speech where a comparison is made using 'like' or 'as'?",
                options = listOf("Simile", "Metaphor", "Alliteration", "Oxymoron"),
                correctOptionIndex = 0,
                imageRelativePath = null,
                audioRelativePath = null,
                quizId = 3L
            ),
            QuestionEntity(
                id = 4L,
                title = "Listen to the audio and identify the type of word being spoken.",
                options = listOf("Adverb", "Noun", "Adjective", "Verb"),
                correctOptionIndex = 3,
                imageRelativePath = null,
                audioRelativePath = "/dummy/sulking.mp3",
                quizId = 3L
            ),
            QuestionEntity(
                id = 5L,
                title = "Which figure of speech involves giving human qualities to inanimate objects?",
                options = listOf("Metaphor", "Personification", "Irony", "Alliteration"),
                correctOptionIndex = 1,
                imageRelativePath = null,
                audioRelativePath = null,
                quizId = 3L
            )
        )
    )

    val materialList= listOf(
        StudyMaterial(
            id = 1,
            name = "Trigonometric identities",
            description = "Trigonometry notes with graphs",
            pdfUri = "/dummy/trigonometry.pdf",
            pages = 62
        ),
        StudyMaterial(
            id = 2,
            name = "Prepositions",
            description = null,
            pdfUri = "/dummy/prepositions.pdf",
            pages = 5
        ),
        StudyMaterial(
            id = 3,
            name = "Classification of Animals",
            description = "Quick description about the animal kingdom",
            pdfUri = "/dummy/animal_kingdom.pdf",
            pages = 9
        ),
        StudyMaterial(
            id = 4,
            name = "Acids and bases",
            description = "Simple video about acids and bases",
            pdfUri = "/dummy/acids_and_bases.mp4",
            pages = 0,
            materialType = MaterialType.VIDEO
        )
    )


    @RequiresApi(Build.VERSION_CODES.O)
    val quizList= listOf(scienceQuizWithQuestions, mathsQuizWithQuestions, englishQuizWithQuestions)

}