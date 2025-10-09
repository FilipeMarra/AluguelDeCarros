"use client"

import { useState, useCallback } from "react"
import { AvaliacaPedidoDto } from "@/app/lib/definitions"
import { criarAvaliacao } from "@/app/lib/data"

export function useAvaliacao() {
  const [isLoading, setIsLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const criarAvaliacaoPedido = useCallback(async (avaliacaoData: AvaliacaPedidoDto) => {
    setIsLoading(true)
    setError(null)
    try {
      await criarAvaliacao(avaliacaoData)
      return true
    } catch (err) {
      setError(err instanceof Error ? err.message : "Erro ao criar avaliação")
      console.error("Erro ao criar avaliação:", err)
      return false
    } finally {
      setIsLoading(false)
    }
  }, [])

  return {
    isLoading,
    error,
    criarAvaliacaoPedido,
    clearError: () => setError(null)
  }
}
