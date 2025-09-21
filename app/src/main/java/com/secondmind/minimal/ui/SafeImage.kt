package com.secondmind.minimal.ui
import androidx.compose.runtime.Composable; import androidx.compose.foundation.layout.*; import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.Modifier; import androidx.compose.ui.Alignment; import androidx.compose.ui.layout.ContentScale; import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
@Composable fun SafeImage(model:Any?,contentDescription:String?,modifier:Modifier=Modifier,contentScale:ContentScale=ContentScale.Crop){
  SubcomposeAsyncImage(model=model,contentDescription=contentDescription,modifier=modifier,contentScale=contentScale,
    loading={ Box(Modifier.fillMaxWidth().height(120.dp), contentAlignment=Alignment.Center){ CircularProgressIndicator() } },
    error={ Box(Modifier.fillMaxWidth().height(120.dp)){} })
}
