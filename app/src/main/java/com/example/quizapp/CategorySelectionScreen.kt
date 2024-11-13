import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quizapp.Question
import com.example.quizapp.R
import com.example.quizapp.historyQuestions
import com.example.quizapp.scienceQuestions
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import com.example.quizapp.geoQuestions
import com.example.quizapp.mathQuestions
import com.example.quizapp.musicQuestions


@Composable
fun CategorySelectionScreen(onCategorySelected: (String, List<Question>) -> Unit) {

    val categories = listOf(
        Triple("Ciências", R.drawable.flask, scienceQuestions),
        Triple("História", R.drawable.landmark_solid, historyQuestions),
        Triple("Matemática", R.drawable.square_root_variable_solid, mathQuestions),
        Triple("Geografia", R.drawable.book_atlas_solid, geoQuestions),
        Triple("Musica", R.drawable.music_solid, musicQuestions)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Escolha uma categoria:", fontSize = 24.sp, color = Color.White)

        Spacer(modifier = Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(categories.size) { index ->
                val (text, iconRes, questions) = categories[index]
                CategoryButton(
                    text = text,
                    icon = painterResource(id = iconRes),
                    onClick = { onCategorySelected(text, questions) }
                )
            }
        }
    }
}

@Composable
fun CategoryButton(text: String, icon: Painter, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.primary,
        onClick = onClick,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp)
        ) {
            Icon(
                painter = icon,
                contentDescription = null,
                modifier = Modifier.size(32.dp), // Made icon slightly larger
                tint = MaterialTheme.colorScheme.onPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = text,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}