package com.hdesrosiers.composescaffold

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hdesrosiers.composescaffold.ui.theme.ComposeScaffoldTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      ComposeScaffoldTheme {
        // A surface container using the 'background' color from the theme
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
//          ScaffoldDemo()
          BackdropDemo()
        }
      }
    }
  }
}

data class NavigationItem(
  val title: String,
  @DrawableRes val iconRes: Int
)

val bottomMenuItems = listOf(
  NavigationItem(title = "Item 1", iconRes = R.drawable.ic_one),
  NavigationItem(title = "Item 2", iconRes = R.drawable.ic_two),
  NavigationItem(title = "Item 3", iconRes = R.drawable.ic_three)
)

val drawerMenuItems = listOf(
  NavigationItem(title = "Section 1", iconRes = R.drawable.ic_one),
  NavigationItem(title = "Section 2", iconRes = R.drawable.ic_two),
  NavigationItem(title = "Section 3", iconRes = R.drawable.ic_three)
)

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ScaffoldDemo() {
  val selectedItem = remember { mutableStateOf(bottomMenuItems.first().title) }

  /**
   * By default, drawer state is closed, but we can customize it.
   *
   * Closed drawer state: `rememberScaffoldState(drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)`
   * Opened drawer state: `rememberScaffoldState(drawerState = rememberDrawerState(initialValue = DrawerValue.Open))`
   */
  val scaffoldState = rememberScaffoldState()
  val scope = rememberCoroutineScope()

  Scaffold(
    scaffoldState = scaffoldState,
    topBar = {
      TopAppBar(
        title = { Text(text = "Title") },
        navigationIcon = {
          IconButton(
            onClick = {
              scope.launch { scaffoldState.drawerState.open() }
            }
          ) {
            Icon(
//              painter = painterResource(id = R.drawable.ic_menu),
              imageVector = Icons.Filled.Menu,
              contentDescription = "Menu"
            )
          }
        }
      )
    },
    bottomBar = {
      BottomNavigation {
        bottomMenuItems.forEach { screen ->
          BottomNavigationItem(
            selected = screen.title == selectedItem.value,
            onClick = { selectedItem.value = screen.title },
            label = { Text(screen.title) },
            icon = {
              Icon(
                painter = painterResource(id = screen.iconRes),
                contentDescription = null
              )
            }
          )
        }
      }
    },
    floatingActionButton = {
      FloatingActionButton(
        onClick = {
          scope.launch {
            scaffoldState.snackbarHostState
              .showSnackbar("Button clicked")
          }
        }
      ) {
        Icon(
//          painter = painterResource(id = R.drawable.ic_add),
          imageVector = Icons.Filled.Add,
          contentDescription = "Add",
          tint = Color.White
        )
      }
    },
    floatingActionButtonPosition = FabPosition.End,
    drawerContent = {
      LazyColumn {
        items(drawerMenuItems.size) {
          ListItem(
            text = {
              Text(
                text = "Category ${drawerMenuItems[it].title}"
              )
            },
            icon = {
              Icon(
                painter = painterResource(id = drawerMenuItems[it].iconRes),
                contentDescription = drawerMenuItems[it].title
              )
            }
          )
        }
      }
    },
    content = {
      Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
      ) {
        Text(
          text = selectedItem.value,
          fontSize = 40.sp,
          textAlign = TextAlign.Center
        )
      }
    },
  )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BackdropDemo() {
  val menuItems = listOf(
    "Item 1",
    "Item 2",
    "Item 3",
    "Item 4",
    "Item 5",
    "Item 6",
    "Item 7",
    "Item 8",
    "Item 9",
    "Item 10"
  )
  val selectedItem = remember { mutableStateOf(menuItems.first()) }
  val scaffoldState = rememberBackdropScaffoldState(BackdropValue.Concealed)
  val scope = rememberCoroutineScope()

  BackdropScaffold(
    scaffoldState = scaffoldState,
    appBar = {
      TopAppBar(
        title = { Text("Title") },
        navigationIcon = {
          if (scaffoldState.isConcealed) {
            IconButton(onClick = { scope.launch { scaffoldState.reveal() } }) {
              Icon(
//                painter = painterResource(id = R.drawable.ic_menu),
                imageVector = Icons.Default.Menu,
                contentDescription = "Open menu"
              )
            }
          } else {
            IconButton(onClick = { scope.launch { scaffoldState.conceal() } }) {
              Icon(
//                painter = painterResource(id = R.drawable.ic_arrow_back),
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Close menu"
              )
            }
          }
        },
        elevation = 0.dp,
        backgroundColor = Color.Transparent
      )
    },
    backLayerContent = {
      LazyColumn(
        modifier = Modifier.fillMaxSize()
      ) {
        itemsIndexed(menuItems) { i, item ->
          ListItem(
            text = { Text(text = menuItems[i]) },
            modifier = Modifier.clickable {
              selectedItem.value = item
              scope.launch { scaffoldState.conceal() }
            }
          )
        }
      }
    },
    frontLayerContent = {
      Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
      ) {
        Text(
          text = selectedItem.value,
          fontSize = 40.sp,
          textAlign = TextAlign.Center
        )
      }
    }
  )
}
































