import {AvaliacaPedidoDto, PedidoAluguel, PedidoAluguelDto, RegisterData } from "./definitions";

// Re-exportar tipos para uso em outros arquivos
export type { PedidoAluguel, PedidoAluguelDto } from "./definitions";

// Função auxiliar para obter headers de autenticação
function getAuthHeaders(token?: string): HeadersInit {
  const headers: HeadersInit = {
    "Content-Type": "application/json",
  }
  
  if (token) {
    headers["Authorization"] = `Bearer ${token}`
  }
  
  return headers
}

export async function RegisterApi(data: RegisterData) {
  const url =
    data.userType === "cliente"
      ? "http://localhost:8081/api/v1/cliente"
      : "http://localhost:8081/api/v1/agente";

  const response = await fetch(url, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(data),
  });

  let result = null;
  try {
    result = await response.json();
  } catch {
    result = null;
  }

  if (!response.ok) {
    throw new Error(result?.message || "Erro ao registrar usuário");
  }

  return result; 
}

export async function listarTodosPedidos(token?: string): Promise<PedidoAluguel[]> {
  try {
    const res = await fetch("http://localhost:8081/api/v1/pedido", {
      headers: getAuthHeaders(token)
    })
    if (!res.ok) {
      throw new Error(`Erro ${res.status}: ${res.statusText}`)
    }
    return res.json()
  } catch (error) {
    console.error("Erro ao listar pedidos:", error)
    throw new Error("Erro ao conectar com o servidor")
  }
}

export async function buscarPedidoPorId(id: string): Promise<PedidoAluguel> {
  try {
    const res = await fetch(`http://localhost:8081/api/v1/pedido/${id}`)
    if (!res.ok) {
      if (res.status === 404) {
        throw new Error("Pedido não encontrado")
      }
      throw new Error(`Erro ${res.status}: ${res.statusText}`)
    }
    return res.json()
  } catch (error) {
    console.error("Erro ao buscar pedido:", error)
    throw error
  }
}

export async function listarPedidosPorStatus(status: string): Promise<PedidoAluguel[]> {
  try {
    const res = await fetch(`http://localhost:8081/api/v1/pedido/status?status=${status}`)
    if (!res.ok) {
      throw new Error(`Erro ${res.status}: ${res.statusText}`)
    }
    return res.json()
  } catch (error) {
    console.error("Erro ao buscar pedidos por status:", error)
    return []
  }
}

export async function criarPedido(pedido: PedidoAluguelDto): Promise<void> {
  try {
    const res = await fetch("http://localhost:8081/api/v1/pedido", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(pedido),
    })

    if (!res.ok) {
      const errorData = await res.json().catch(() => null)
      throw new Error(errorData?.message || `Erro ${res.status}: ${res.statusText}`)
    }
  } catch (error) {
    console.error("Erro ao criar pedido:", error)
    throw error
  }
}

/**
 * Deleta um pedido de aluguel
 */
export async function deletarPedido(id: string): Promise<void> {
  try {
    const res = await fetch(`http://localhost:8081/api/v1/pedido/${id}`, {
      method: "DELETE",
    })

    if (!res.ok) {
      if (res.status === 404) {
        throw new Error("Pedido não encontrado")
      }
      throw new Error(`Erro ${res.status}: ${res.statusText}`)
    }
  } catch (error) {
    console.error("Erro ao deletar pedido:", error)
    throw error
  }
}

export async function criarAvaliacao(request: AvaliacaPedidoDto): Promise<void> {
  try {
    const res = await fetch("http://localhost:8081/api/v1/avaliacao", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(request),
    })

    if (!res.ok) {
      const errorData = await res.json().catch(() => null)
      throw new Error(errorData?.message || `Erro ${res.status}: ${res.statusText}`)
    }
  } catch (error) {
    console.error("Erro ao criar avaliação:", error)
    throw error
  }
}

export async function listarTodasAvaliacoes(): Promise<AvaliacaPedidoDto[]> {
  const res = await fetch("http://localhost:8081/api/v1/avaliacao")
  if (!res.ok) throw new Error("Erro ao listar avaliações")
  return res.json()
}

export async function listarAvaliacaoPorId(id: string): Promise<AvaliacaPedidoDto> {
  const res = await fetch(`http://localhost:8081/api/v1/avaliacao/${id}`)
  if (!res.ok) throw new Error("Avaliação não encontrada")
  return res.json()
}


export async function atualizarAvaliacao(request: AvaliacaPedidoDto): Promise<void> {
  const res = await fetch("http://localhost:8081/api/v1/avaliacao/atualizar", {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(request),
  })

  if (!res.ok) throw new Error("Erro ao atualizar avaliação")
}

// ===== FUNÇÕES PARA VEÍCULOS =====

export interface VeiculoDto {
  ano: number
  marca: string
  modelo: string
  placa: string
  valorMensal: number
}

export interface Veiculo {
  matricula: number
  ano: number
  marca: string
  modelo: string
  placa: string
  valorMensal: number
}

/**
 * Lista todos os veículos
 */
export async function listarTodosVeiculos(): Promise<Veiculo[]> {
  try {
    const res = await fetch("http://localhost:8081/api/v1/automovel")
    if (!res.ok) {
      throw new Error(`Erro ${res.status}: ${res.statusText}`)
    }
    return res.json()
  } catch (error) {
    console.error("Erro ao listar veículos:", error)
    throw new Error("Erro ao conectar com o servidor")
  }
}

export async function criarVeiculo(veiculo: VeiculoDto): Promise<Veiculo> {
  console.log("Sending to API:", veiculo); // Add this line
  try {
    const res = await fetch("http://localhost:8081/api/v1/automovel", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(veiculo),
    });

    console.log("API Response Status:", res.status);
    console.log("API Response Headers:", Object.fromEntries(res.headers.entries()));
    const text = await res.text();
    console.log("API Response Text:", text);

    if (!res.ok) {
      const errorData = text || `Erro ${res.status}: ${res.statusText}`;
      throw new Error(errorData);
    }

    const contentLength = res.headers.get("content-length");
    if (contentLength && parseInt(contentLength) === 0) {
      const updatedVeiculos = await listarTodosVeiculos();
      const newVeiculo = updatedVeiculos.find(v => v.placa === veiculo.placa);
      if (!newVeiculo) throw new Error("Novo veículo não encontrado após criação");
      console.log("Fallback Veiculo:", newVeiculo);
      return newVeiculo;
    }

    const data = await res.json();
    console.log("Parsed JSON Data:", data);
    return data;
  } catch (error) {
    console.error("Erro ao criar veículo:", error);
    throw error;
  }
}

/**
 * Atualiza um veículo
 */
export async function atualizarVeiculo(veiculo: VeiculoDto & { matricula: number }): Promise<Veiculo> {
  try {
    const res = await fetch("http://localhost:8081/api/v1/automovel", {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(veiculo),
    });

    if (!res.ok) {
      const errorData = await res.text().catch(() => null);
      throw new Error(errorData || `Erro ${res.status}: ${res.statusText}`);
    }

    // Check if response has a body before calling json()
    const contentLength = res.headers.get("content-length");
    if (contentLength && parseInt(contentLength) === 0) {
      // If no content, fetch the updated vehicle list and find the updated vehicle
      const updatedVeiculos = await listarTodosVeiculos();
      const updatedVeiculo = updatedVeiculos.find(v => v.matricula === veiculo.matricula);
      if (!updatedVeiculo) throw new Error("Veículo atualizado não encontrado");
      return updatedVeiculo;
    }

    return await res.json();
  } catch (error) {
    console.error("Erro ao atualizar veículo:", error);
    throw error;
  }
}

/**
 * Deleta um veículo
 */
export async function deletarVeiculo(matricula: number): Promise<void> {
  try {
    const res = await fetch(`http://localhost:8081/api/v1/automovel/${matricula}`, {
      method: "DELETE",
    })

    if (!res.ok) {
      throw new Error(`Erro ${res.status}: ${res.statusText}`)
    }
  } catch (error) {
    console.error("Erro ao deletar veículo:", error)
    throw error
  }
}

