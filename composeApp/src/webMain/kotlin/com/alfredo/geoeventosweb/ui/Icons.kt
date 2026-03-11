package com.alfredo.geoeventosweb.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Icono lupa dibujado con Canvas — sin dependencias de fuentes ni emojis.
 */
@Composable
fun IconSearch(
    tint:   Color = Color(0xFF3A6FC4),
    size:   Dp    = 20.dp
) {
    Canvas(modifier = Modifier.size(size)) {
        val s        = this.size
        val stroke   = Stroke(width = s.width * 0.12f, cap = StrokeCap.Round)
        val radius   = s.width * 0.30f
        val cx       = s.width  * 0.40f
        val cy       = s.height * 0.40f

        // Círculo de la lupa
        drawCircle(
            color  = tint,
            radius = radius,
            center = Offset(cx, cy),
            style  = stroke
        )
        // Mango de la lupa
        val startAngle45 = radius * 0.70f
        drawLine(
            color       = tint,
            start       = Offset(cx + startAngle45, cy + startAngle45),
            end         = Offset(s.width * 0.88f, s.height * 0.88f),
            strokeWidth = s.width * 0.12f,
            cap         = StrokeCap.Round
        )
    }
}

/**
 * Icono X (cerrar/limpiar) dibujado con Canvas.
 */
@Composable
fun IconClose(
    tint: Color = Color(0xFF9AA3B2),
    size: Dp    = 14.dp
) {
    Canvas(modifier = Modifier.size(size)) {
        val s       = this.size
        val padding = s.width * 0.20f
        val stroke  = s.width * 0.14f

        drawLine(
            color       = tint,
            start       = Offset(padding, padding),
            end         = Offset(s.width - padding, s.height - padding),
            strokeWidth = stroke,
            cap         = StrokeCap.Round
        )
        drawLine(
            color       = tint,
            start       = Offset(s.width - padding, padding),
            end         = Offset(padding, s.height - padding),
            strokeWidth = stroke,
            cap         = StrokeCap.Round
        )
    }
}

/**
 * Flecha apuntando a la izquierda — botón Volver.
 */
@Composable
fun IconBack(
    tint: Color = Color(0xFF3A6FC4),
    size: Dp    = 16.dp
) {
    Canvas(modifier = Modifier.size(size)) {
        val s       = this.size
        val stroke  = s.width * 0.14f
        val midY    = s.height * 0.50f
        val tipX    = s.width  * 0.18f
        val tailX   = s.width  * 0.82f
        val armLen  = s.height * 0.30f

        // Línea horizontal
        drawLine(
            color       = tint,
            start       = Offset(tipX, midY),
            end         = Offset(tailX, midY),
            strokeWidth = stroke,
            cap         = StrokeCap.Round
        )
        // Brazo superior de la flecha
        drawLine(
            color       = tint,
            start       = Offset(tipX, midY),
            end         = Offset(tipX + armLen, midY - armLen),
            strokeWidth = stroke,
            cap         = StrokeCap.Round
        )
        // Brazo inferior de la flecha
        drawLine(
            color       = tint,
            start       = Offset(tipX, midY),
            end         = Offset(tipX + armLen, midY + armLen),
            strokeWidth = stroke,
            cap         = StrokeCap.Round
        )
    }
}

/**
 * Chevron apuntando a la derecha — indicador de fila en lista.
 */
@Composable
fun IconChevronRight(
    tint: Color = Color(0xFF9AA3B2),
    size: Dp    = 14.dp
) {
    Canvas(modifier = Modifier.size(size)) {
        val s      = this.size
        val stroke = s.width * 0.16f
        val midX   = s.width  * 0.38f
        val arm    = s.height * 0.28f
        val midY   = s.height * 0.50f

        drawLine(
            color       = tint,
            start       = Offset(midX, midY - arm),
            end         = Offset(midX + arm, midY),
            strokeWidth = stroke,
            cap         = StrokeCap.Round
        )
        drawLine(
            color       = tint,
            start       = Offset(midX + arm, midY),
            end         = Offset(midX, midY + arm),
            strokeWidth = stroke,
            cap         = StrokeCap.Round
        )
    }
}