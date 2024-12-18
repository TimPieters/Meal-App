package com.example.app.UIUX.screens

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.app.SharedViewModel
import com.example.app.TopBar
import com.example.app.R
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.font.FontWeight.Companion.Medium
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.app.Ingredient
import com.example.app.Instruction
import com.example.app.Recipe
import kotlin.math.max
import kotlin.math.min

val AppBarCollapsedHeight = 56.dp
val AppBarExpendedHeight = 400.dp

@Composable
fun RecipeScreen(mealId: Int, navController: NavHostController, sharedViewModel: SharedViewModel) {
    val meals by sharedViewModel.generatedRecipes.observeAsState(emptyList())
    val meal = meals.getOrNull(mealId)
    val scrollState = rememberLazyListState() // Remember the LazyColumn scroll state

    Box {
        meal?.let { Content(it, scrollState) }
        meal?.let { ParallaxToolbar(it, scrollState, navController) }
    }
}

@Composable
fun Content(meal: Recipe, scrollState: LazyListState) {
    LazyColumn(
        contentPadding = PaddingValues(top = AppBarExpendedHeight),
        state = scrollState
    ) {
        item {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                BasicInfo(meal)
                Spacer(modifier = Modifier.height(8.dp))
                Description(meal.description)
                Spacer(modifier = Modifier.height(16.dp))
                TabSection(meal)
                CookAlongSection(onClick = {})
            }
        }
    }
}

@Composable
fun Description(description: String) {
    Text(
        text = description,
        fontSize = 16.sp,
        fontWeight = FontWeight.Light,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    )
    Divider(thickness = 1.dp, modifier = Modifier.padding(vertical = 16.dp))
}

@Composable
fun TabSection(meal: Recipe) {
    var selectedTab by remember { mutableStateOf("Ingredients") }

    Column {
        // Tab Buttons
        Row(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            listOf("Ingredients", "Steps").forEach { tab ->
                TabButton(
                    text = tab,
                    active = selectedTab == tab,
                    onClick = { selectedTab = tab }
                )
            }
        }

        // Tab Content
        when (selectedTab) {
            "Ingredients" -> IngredientsGrid(meal)
            "Steps" -> StepsList(meal.instructions)
        }
    }
}

@Composable
fun IngredientsList(ingredients: List<Ingredient>) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Ingredients",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        ingredients.forEach { ingredient ->
            Text(
                text = "- ${ingredient.quantity} ${ingredient.name}",
                fontSize = 16.sp,
                modifier = Modifier.padding(horizontal = 16.dp,vertical = 4.dp)
            )
        }
    }
}

@Composable
fun IngredientsGrid(recipe: Recipe) {
    EasyGrid(nColumns = 3, items = recipe.ingredients) {
        IngredientCard(getImageResourceForIngredient(it.name), it.name, it.quantity, Modifier)
    }

}

@Composable
fun <T> EasyGrid(nColumns: Int, items: List<T>, content: @Composable (T) -> Unit) {
    Column(Modifier.padding(16.dp)) {
        for (i in items.indices step nColumns) {
            Row {
                for (j in 0 until nColumns) {
                    if (i + j < items.size) {
                        Box(
                            contentAlignment = Alignment.TopCenter,
                            modifier = Modifier.weight(1f)
                        ) {
                            content(items[i + j])
                        }
                    } else {
                        Spacer(Modifier.weight(1f, fill = true))
                    }
                }
            }
        }
    }
}


@Composable
fun IngredientCard(
    @DrawableRes iconResource: Int,
    name: String,
    quantity: String,
    modifier: Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(bottom = 16.dp)
    ) {
        Card(
            shape = CardDefaults.shape,
            elevation = CardDefaults.cardElevation(
                defaultElevation = 2.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            ),
            modifier = Modifier
                .width(100.dp)
                .height(100.dp)
                .padding(bottom = 8.dp)
        ) {
            Image(
                painter = painterResource(id = iconResource),
                contentDescription = null,
                modifier = Modifier.padding(16.dp)
            )
        }
        Text(text = name, modifier = Modifier.width(100.dp), fontSize = 14.sp, fontWeight = Medium)
        Text(text = quantity, color = DarkGray, modifier = Modifier.width(100.dp), fontSize = 14.sp)
    }
}

@Composable
fun StepsList(instructions: List<Instruction>) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Instructions",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        instructions.forEachIndexed { index, instruction ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text(
                    text = "Step ${index + 1}: ${instruction.step}",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
                )
                Text(
                    text = "Approximate Time: ${instruction.approximate_time}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Light,
                    modifier = Modifier.padding(bottom = 8.dp, start = 16.dp)
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParallaxToolbar(
    meal: Recipe,
    scrollState: LazyListState,
    navController: NavHostController
) {
    val density = LocalDensity.current
    val expandedHeightPx = with(density) { AppBarExpendedHeight.toPx() }
    val collapsedHeightPx = with(density) { AppBarCollapsedHeight.toPx() }
    val totalScrollRange = expandedHeightPx - collapsedHeightPx

    val scrollOffset = scrollState.firstVisibleItemScrollOffset.coerceIn(0, totalScrollRange.toInt())
    val offsetProgress = scrollOffset / totalScrollRange

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(AppBarExpendedHeight)
    ) {
        // Parallax Image
        Image(
            painter = painterResource(id = R.drawable.dish),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(AppBarExpendedHeight)
                .graphicsLayer {
                    translationY = -offsetProgress * (expandedHeightPx - collapsedHeightPx) // Smooth translation
                    alpha = 1f - offsetProgress // Smooth fade-out
                }
        )

        // Dynamic Gradient Overlay (part of the scrolling effect)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(AppBarExpendedHeight) // Ensure it matches the height of the toolbar
                .graphicsLayer {
                    translationY = -offsetProgress * (expandedHeightPx - collapsedHeightPx) // Move gradient with image
                }
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            MaterialTheme.colorScheme.surface // White gradient
                        )
                    )
                )
        )

        // Title Positioned Dynamically
        Text(
            text = meal.name,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 3,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(
                    start = (16.dp + (40.dp - 16.dp) * offsetProgress), // Move towards center of TopAppBar
                    top = (AppBarExpendedHeight - AppBarCollapsedHeight) * (1 - offsetProgress) // Move upwards
                )
                .graphicsLayer {
                    scaleX = 1f - 0.15f * offsetProgress // Scale slightly
                    scaleY = 1f - 0.15f * offsetProgress
                }
        )
    }

    // Always-visible TopAppBar
    TopAppBar(
        title = {},
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_back),
                    contentDescription = "Back"
                )
            }
        },
        actions = {
            IconButton(onClick = { /* Handle favorite logic */ }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_favorite),
                    contentDescription = "Favorite"
                )
            }
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = Color.Transparent,
            navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
            actionIconContentColor = MaterialTheme.colorScheme.onSurface
        ),
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
    )
}

@Composable
fun TabButton(text: String, active: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .height(40.dp)
            .padding(horizontal = 8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (active) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
            contentColor = if (active) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
        ),
        elevation = ButtonDefaults.buttonElevation(0.dp)
    ) {
        Text(text, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun BasicInfo(meal: Recipe) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(MaterialTheme.colorScheme.surface)
            .padding(vertical = 12.dp) // Inner padding for spacing within the bar
    ) {
        InfoColumn(R.drawable.ic_clock, meal.estimated_total_time, "Time")
        InfoColumn(R.drawable.ic_difficulty, meal.difficulty.capitalize(), "Difficulty")
    }
}

@Composable
fun InfoColumn(
    @DrawableRes iconResource: Int,
    text: String,
    label: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            painter = painterResource(id = iconResource),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = text, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Text(text = label, color = Color.Gray, fontSize = 12.sp)
    }
}

@Composable
fun CookAlongSection(onClick: () -> Unit) {
    var isVisible by remember { mutableStateOf(false) } // Track visibility state
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                        Color.Transparent
                    )
                ),
                shape = MaterialTheme.shapes.medium
            )
            .clip(MaterialTheme.shapes.medium)
            .padding(16.dp)
            .onGloballyPositioned { layoutCoordinates ->
                isVisible = layoutCoordinates.parentLayoutCoordinates?.let {
                    val parentBounds = it.boundsInWindow()
                    val childBounds = layoutCoordinates.boundsInWindow()
                    parentBounds.overlaps(childBounds)
                } ?: false
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Conditionally display LottieAnimation when visible
            if (isVisible) {
                val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.chef_cooking_animation))
                val progress by animateLottieCompositionAsState(
                    composition = composition,
                    iterations = 1 // Play animation once
                )

                LottieAnimation(
                    composition = composition,
                    progress = progress,
                    modifier = Modifier.size(140.dp)
                )
            }
            // Title
            Text(
                text = "Cook Along Mode",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            // Description
            Text(
                text = "Make cooking fun and interactive! Follow step-by-step instructions with animations for every step.",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
            )
            // Action Button
            Button(
                onClick = onClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    text = "Start Now",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}


class PreviewSharedViewModel : SharedViewModel() {
    init {
        setGeneratedRecipes(
            listOf(
                Recipe(
                    name = "Cheesy Sausage and Vegetable Bake",
                    description = "Packed with flavor and nutrition, this recipe combines fresh vegetables, gooey cheese, and hearty sausages. Perfect for a quick dinner!",
                    ingredients = listOf(
                        Ingredient("Sausages", "2"),
                        Ingredient("Bell Pepper","1"),
                        Ingredient("Broccoli","100g"),
                        Ingredient("Cheese","50g"),
                        Ingredient("Olive Oil","1tsp")),
                    instructions = listOf(
                        Instruction("Preheat the oven to 200°C.", "5 mins"),
                        Instruction("Chop all vegetables into bite-sized pieces.", "10 mins"),
                        Instruction("Mix sausages, vegetables, and olive oil in a baking tray.", "5 mins"),
                        Instruction("Bake in the oven for 30 minutes until golden brown.", "30 mins")
                    ),
                    estimated_total_time = "45 minutes",
                    difficulty = "Easy",
                    serving_size = 4,
                    nutritional_info = "350 kcal per serving"
                )
            )
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun RecipeScreenPreview() {
    // Mock NavHostController
    val mockNavController = rememberNavController()
    // Use PreviewSharedViewModel with mock data
    val sharedViewModel = PreviewSharedViewModel()
    RecipeScreen(
        mealId = 0, // Assuming the first recipe in the mock list has ID 0
        navController = mockNavController,
        sharedViewModel = sharedViewModel
    )
}




