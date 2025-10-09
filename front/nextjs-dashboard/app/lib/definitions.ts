export type UserType = "cliente" | "agente"
export type AgentType = "empresa" | "banco"

export interface ClienteData {
  nome: string
  email: string
  senha: string
  cpf: string
  registro: string
  profissao: string
}

export interface AgenteData {
  nome: string
  email: string
  senha: string
  agentType: AgentType
}

export type RegisterData =
  | ({ userType: "cliente" } & ClienteData)
  | ({ userType: "agente" } & AgenteData)


export interface PedidoAluguel {
  id: string
  status: string
  automovel?: {
    marca: string
    modelo: string
    ano: number
    placa: string
    precoMensal: number
  }
  clienteSolicitante?: {
    nome: string
    email: string
    cpf: string
  }
  valorMensal?: number
  dataSolicitacao?: string
  dataInicio?: string
  dataFim?: string
  creditoAssociado?: boolean
  observacoes?: string // Add this field
}

export interface PedidoAluguelDto {
  automovel: {
    marca: string
    modelo: string
    ano: number
    placa: string
  }
  dataInicio: string // Formato: YYYY-MM-DD
  dataFim: string // Formato: YYYY-MM-DD
  valorMensal: number

}

export interface AvaliacaPedidoDto {
  idPedido: string           // UUID do pedido
  emailAgente: string
  data: string               // formato YYYY-MM-DD
  parecer: string
}
