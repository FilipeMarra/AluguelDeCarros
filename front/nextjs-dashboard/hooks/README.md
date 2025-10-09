# Hooks Customizados

Este diretório contém hooks customizados para gerenciar o estado da aplicação.

## usePedidos

Hook para gerenciar pedidos de aluguel com integração completa ao backend.

### Uso Básico

```tsx
import { usePedidos } from "@/hooks/use-pedidos"

function MeuComponente() {
  const { 
    pedidos, 
    isLoading, 
    error, 
    criarNovoPedido, 
    removerPedido 
  } = usePedidos()

  const handleCriarPedido = async () => {
    const sucesso = await criarNovoPedido({
      automovel: {
        marca: "Toyota",
        modelo: "Corolla",
        ano: 2024,
        placa: "ABC-1234"
      },
      dataInicio: "2024-01-01",
      dataFim: "2024-12-31",
      valorMensal: 2500,
      creditoAssociado: true
    })

    if (sucesso) {
      console.log("Pedido criado com sucesso!")
    }
  }

  return (
    <div>
      {isLoading && <p>Carregando...</p>}
      {error && <p>Erro: {error}</p>}
      {pedidos.map(pedido => (
        <div key={pedido.id}>
          {pedido.automovel.marca} {pedido.automovel.modelo}
        </div>
      ))}
    </div>
  )
}
```

### usePedidoStatus

Hook para buscar pedidos por status específico.

```tsx
import { usePedidoStatus } from "@/hooks/use-pedidos"

function PedidosPendentes() {
  const { pedidos, isLoading, error } = usePedidoStatus("pendente")

  return (
    <div>
      {pedidos.map(pedido => (
        <div key={pedido.id}>
          {pedido.automovel.marca} {pedido.automovel.modelo}
        </div>
      ))}
    </div>
  )
}
```

## useToast

Hook para exibir notificações toast na aplicação.

### Uso

```tsx
import { useToast } from "@/hooks/use-toast"

function MeuComponente() {
  const { toast } = useToast()

  const handleClick = () => {
    toast({
      title: "Sucesso",
      description: "Operação realizada com sucesso!",
    })

    // Ou para erro
    toast({
      title: "Erro",
      description: "Algo deu errado!",
      variant: "destructive",
    })
  }

  return <button onClick={handleClick}>Clique aqui</button>
}
```
