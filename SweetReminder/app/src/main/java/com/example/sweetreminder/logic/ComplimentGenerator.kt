package com.example.sweetreminder.logic
object ComplimentGenerator {
    private val themes = listOf(
        "appearance", "talent", "kindness",
        "shared_memory", "future_plan", "gratitude"
    )

    private val sentences = mapOf(
        "appearance" to listOf(
            "你的笑容像初夏的微风，温柔却令人无法忽视。",
            "今天的你光彩照人，仿佛把整个春天都穿在了身上。"
        ),
        "talent" to listOf(
            "你解决问题的方式总是那么优雅高效，我真心佩服。",
            "从你身上我看到了专注与创造力完美结合的样子。"
        ),
        "kindness" to listOf(
            "你对世界的善意，让人感到温暖而踏实。",
            "和你相处的日子，总能感受到你体贴入微的关怀。"
        ),
        "shared_memory" to listOf(
            "还记得我们第一次旅行吗？那一刻的你让我怦然心动至今。",
            "与你走过的每条街，都镌刻成我最珍贵的记忆。"
        ),
        "future_plan" to listOf(
            "想到未来与你肩并肩，我就充满了勇气和期待。",
            "有你在身旁，任何计划都像被注入了光亮与可能。"
        ),
        "gratitude" to listOf(
            "谢谢你一直包容我的不足，让我变得更好。",
            "你的支持是我前行的底气，感激之情难以言表。"
        )
    )

    fun next(lastTheme: String?): Pair<String, String> {
        val pool = if (lastTheme == null) themes else themes - lastTheme
        val theme = pool.random()
        val sentence = sentences.getValue(theme).random()
        return theme to sentence
    }
}
