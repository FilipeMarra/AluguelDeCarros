"use client"

import { useState, useEffect, useCallback } from "react"
import { PedidoAluguel, PedidoAluguelDto } from "@/app/lib/definitions"
import { 
  listarTodosPedidos, 
  buscarPedidoPorId, 
  listarPedidosPorStatus, 
  criarPedido, 
  deletarPedido 
} from "@/app/lib/data"
import { useAuth } from "./use-auth"

export function usePedidos() {
  const [pedidos, setPedidos] = useState<PedidoAluguel[]>([])
  const [isLoading, setIsLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)
  const { token } = useAuth()

  const fetchPedidos = useCallback(async () => {
    setIsLoading(true)
    setError(null)
    try {
      const data = await listarTodosPedidos(token || undefined)
      console.log("Fetched pedidos data:", data) // Debug log
      setPedidos(data)
    } catch (err) {
      setError(err instanceof Error ? err.message : "Erro ao carregar pedidos")
      console.error("Erro ao buscar pedidos:", err)
    } finally {
      setIsLoading(false)
    }
  }, [token])

  const fetchPedidosPorStatus = useCallback(async (status: string) => {
    setIsLoading(true)
    setError(null)
    try {
      const data = await listarPedidosPorStatus(status)
      console.log("Fetched pedidos por status data:", data) // Debug log
      setPedidos(data)
    } catch (err) {
      setError(err instanceof Error ? err.message : "Erro ao carregar pedidos")
      console.error("Erro ao buscar pedidos por status:", err)
    } finally {
      setIsLoading(false)
    }
  }, [])

  const criarNovoPedido = useCallback(async (pedidoData: PedidoAluguelDto) => {
    setIsLoading(true)
    setError(null)
    try {
      await criarPedido(pedidoData)
      await fetchPedidos() // Recarrega a lista
      return true
    } catch (err) {
      setError(err instanceof Error ? err.message : "Erro ao criar pedido")
      console.error("Erro ao criar pedido:", err)
      return false
    } finally {
      setIsLoading(false)
    }
  }, [fetchPedidos])

  const removerPedido = useCallback(async (id: string) => {
    setIsLoading(true)
    setError(null)
    try {
      await deletarPedido(id)
      setPedidos(prev => prev.filter(pedido => pedido.id !== id))
      return true
    } catch (err) {
      setError(err instanceof Error ? err.message : "Erro ao deletar pedido")
      console.error("Erro ao deletar pedido:", err)
      return false
    } finally {
      setIsLoading(false)
    }
  }, [])

  const buscarPedido = useCallback(async (id: string) => {
    setIsLoading(true)
    setError(null)
    try {
      const pedido = await buscarPedidoPorId(id)
      console.log("Fetched pedido by ID:", pedido) // Debug log
      return pedido
    } catch (err) {
      setError(err instanceof Error ? err.message : "Erro ao buscar pedido")
      console.error("Erro ao buscar pedido:", err)
      return null
    } finally {
      setIsLoading(false)
    }
  }, [])

  useEffect(() => {
    fetchPedidos()
  }, [fetchPedidos])

  return {
    pedidos,
    isLoading,
    error,
    fetchPedidos,
    fetchPedidosPorStatus,
    criarNovoPedido,
    removerPedido,
    buscarPedido,
    clearError: () => setError(null)
  }
}

// No changes to usePedidoStatus for now, as the issue is primarily with usePedidos
export function usePedidoStatus(status: string) {
  const [pedidos, setPedidos] = useState<PedidoAluguel[]>([])
  const [isLoading, setIsLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const fetchPedidos = useCallback(async () => {
    setIsLoading(true)
    setError(null)
    try {
      const data = await listarPedidosPorStatus(status)
      setPedidos(data)
    } catch (err) {
      setError(err instanceof Error ? err.message : "Erro ao carregar pedidos")
      console.error("Erro ao buscar pedidos por status:", err)
    } finally {
      setIsLoading(false)
    }
  }, [status])

  useEffect(() => {
    fetchPedidos()
  }, [fetchPedidos])

  return {
    pedidos,
    isLoading,
    error,
    refetch: fetchPedidos,
    clearError: () => setError(null)
  }
}