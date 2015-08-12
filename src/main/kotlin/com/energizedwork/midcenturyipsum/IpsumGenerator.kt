package com.energizedwork.midcenturyipsum

interface IpsumGenerator {

  fun paragraphs(count: Int): Collection<String>

}