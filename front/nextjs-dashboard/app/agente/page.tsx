"use client"

import { useState, useEffect } from "react"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Badge } from "@/components/ui/badge"
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs"
import {
    Dialog,
    DialogContent,
    DialogDescription,
    DialogFooter,
    DialogHeader,
    DialogTitle,
} from "@/components/ui/dialog"
import { Label } from "@/components/ui/label"
import { Textarea } from "@/components/ui/textarea"
import { Input } from "@/components/ui/input"
import { Car, CheckCircle, XCircle, LogOut, Building2, Eye, Plus, Edit, Trash2, Loader2 } from "lucide-react"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { usePedidos } from "@/hooks/use-pedidos"
import { useVeiculos } from "@/hooks/use-veiculos"
import { useToast } from "@/hooks/use-toast"
import { PedidoAluguel, Veiculo } from "@/app/lib/data"

type PedidoStatus = "pendente" | "em_analise" | "aprovado" | "rejeitado" | "cancelado"

const statusColors: Record<PedidoStatus, string> = {
    pendente: "bg-yellow-500",
    em_analise: "bg-blue-500",
    aprovado: "bg-green-500",
    rejeitado: "bg-red-500",
    cancelado: "bg-gray-500",
}

const statusLabels: Record<PedidoStatus, string> = {
    pendente: "Pendente",
    em_analise: "Em Análise",
    aprovado: "Aprovado",
    rejeitado: "Rejeitado",
    cancelado: "Cancelado",
}

export default function AgenteHomePage() {
    const { toast } = useToast()
    const { pedidos, isLoading: pedidosLoading, error: pedidosError } = usePedidos()
    const {
        veiculos,
        isLoading: veiculosLoading,
        error: veiculosError,
        criarNovoVeiculo,
        atualizarVeiculoExistente,
        removerVeiculo,
    } = useVeiculos()

    // Local state to manage pedidos, initialized with data from usePedidos
    const [localPedidos, setLocalPedidos] = useState<PedidoAluguel[]>([])
    const [selectedPedido, setSelectedPedido] = useState<PedidoAluguel | null>(null)
    const [isDialogOpen, setIsDialogOpen] = useState(false)
    const [observacoes, setObservacoes] = useState("")
    const [novoStatus, setNovoStatus] = useState<PedidoStatus>("em_analise")

    const [isVeiculoDialogOpen, setIsVeiculoDialogOpen] = useState(false)
    const [editingVeiculo, setEditingVeiculo] = useState<Veiculo | null>(null)
    const [veiculoForm, setVeiculoForm] = useState({
        matricula: "",
        ano: new Date().getFullYear(),
        marca: "",
        modelo: "",
        placa: "",
        valorMensal: 0,
    })

    // Sync localPedidos with pedidos from usePedidos when it changes
    useEffect(() => {
        setLocalPedidos(pedidos)
    }, [pedidos])

    useEffect(() => {
        if (pedidosError) {
            toast({
                title: "Erro",
                description: pedidosError,
                variant: "destructive",
            })
        }
    }, [pedidosError, toast])

    useEffect(() => {
        if (veiculosError) {
            toast({
                title: "Erro",
                description: veiculosError,
                variant: "destructive",
            })
        }
    }, [veiculosError, toast])

    const handleAvaliarPedido = (pedido: PedidoAluguel) => {
        setSelectedPedido(pedido)
        setObservacoes(pedido.observacoes || "") // Pre-fill with existing observations
        setNovoStatus(pedido.status as PedidoStatus)
        setIsDialogOpen(true)
    }

    // Update pedido status locally, preserving existing observacoes unless a new one is provided
    const updatePedidoStatus = (pedidoId: string, newStatus: PedidoStatus, observacao?: string) => {
        setLocalPedidos((prevPedidos) =>
            prevPedidos.map((pedido) =>
                pedido.id === pedidoId
                    ? {
                        ...pedido,
                        status: newStatus,
                        observacoes: observacao !== undefined && observacao !== "" ? observacao : pedido.observacoes || "",
                    }
                    : pedido
            )
        )
        toast({
            title: "Sucesso",
            description: `Status do pedido alterado para "${statusLabels[newStatus]}" com sucesso!`,
        })
    }

    const handleModificarStatus = async () => {
        if (!selectedPedido) {
            toast({
                title: "Erro",
                description: "Nenhum pedido selecionado.",
                variant: "destructive",
            })
            return
        }
        updatePedidoStatus(selectedPedido.id, novoStatus, observacoes || undefined) // Only pass observacao if it exists
        setIsDialogOpen(false)
        setSelectedPedido(null)
        setObservacoes("")
    }

    const handleRejeitar = async () => {
        if (!selectedPedido) {
            toast({
                title: "Erro",
                description: "Nenhum pedido selecionado.",
                variant: "destructive",
            })
            return
        }
        updatePedidoStatus(selectedPedido.id, "rejeitado", observacoes || "Pedido rejeitado")
        setIsDialogOpen(false)
        setSelectedPedido(null)
        setObservacoes("")
    }

    const handleAprovar = async () => {
        if (!selectedPedido) {
            toast({
                title: "Erro",
                description: "Nenhum pedido selecionado.",
                variant: "destructive",
            })
            return
        }
        updatePedidoStatus(selectedPedido.id, "aprovado", observacoes || "Pedido aprovado")
        setIsDialogOpen(false)
        setSelectedPedido(null)
        setObservacoes("")
    }

    const handleColocarEmAnalise = async () => {
        if (!selectedPedido) {
            toast({
                title: "Erro",
                description: "Nenhum pedido selecionado.",
                variant: "destructive",
            })
            return
        }
        updatePedidoStatus(selectedPedido.id, "em_analise", observacoes || "Pedido colocado em análise")
        setIsDialogOpen(false)
        setSelectedPedido(null)
        setObservacoes("")
    }

    const handleOpenVeiculoDialog = (veiculo?: Veiculo) => {
        if (veiculo) {
            setEditingVeiculo(veiculo)
            setVeiculoForm({
                matricula: veiculo.matricula.toString(),
                ano: veiculo.ano,
                marca: veiculo.marca,
                modelo: veiculo.modelo,
                placa: veiculo.placa,
                valorMensal: veiculo.valorMensal || 0,
            })
        } else {
            setEditingVeiculo(null)
            setVeiculoForm({
                matricula: "",
                ano: new Date().getFullYear(),
                marca: "",
                modelo: "",
                placa: "",
                valorMensal: 0,
            })
        }
        setIsVeiculoDialogOpen(true)
    }

    const handleSaveVeiculo = async () => {
        const veiculoData = {
            ano: veiculoForm.ano,
            marca: veiculoForm.marca,
            modelo: veiculoForm.modelo,
            placa: veiculoForm.placa,
            valorMensal: veiculoForm.valorMensal || 0,
        }

        let success = false
        if (editingVeiculo) {
            success = await atualizarVeiculoExistente(editingVeiculo.matricula, veiculoData)
        } else {
            success = await criarNovoVeiculo(veiculoData)
        }

        if (success) {
            toast({
                title: "Sucesso",
                description: editingVeiculo ? "Veículo atualizado com sucesso!" : "Veículo criado com sucesso!",
            })
            // Recarrega manualmente a lista de veículos
            window.location.reload() // Solução temporária para recarregar a página
        } else {
            toast({
                title: "Erro",
                description: editingVeiculo ? "Não foi possível atualizar o veículo" : "Não foi possível criar o veículo",
                variant: "destructive",
            })
        }

        setIsVeiculoDialogOpen(false)
        setEditingVeiculo(null)
    }

    const handleDeleteVeiculo = async (matricula: number) => {
        if (confirm("Tem certeza que deseja excluir este veículo?")) {
            const success = await removerVeiculo(matricula)
            if (success) {
                toast({
                    title: "Sucesso",
                    description: "Veículo removido com sucesso!",
                })
                // Recarrega manualmente a lista de veículos
                window.location.reload() // Solução temporária para recarregar a página
            } else {
                toast({
                    title: "Erro",
                    description: "Não foi possível remover o veículo",
                    variant: "destructive",
                })
            }
        }
    }

    const filterPedidos = (status?: PedidoStatus) => {
        if (!status) return localPedidos
        return localPedidos.filter((p) => p.status === status)
    }

    const analisarViabilidade = (pedido: PedidoAluguel) => {
        const valorMensal = pedido.valorMensal
        if (!valorMensal || valorMensal <= 0) return { status: "Indefinido", color: "text-gray-600" }
        if (valorMensal <= 2000) return { status: "Excelente", color: "text-green-600" }
        if (valorMensal <= 3000) return { status: "Bom", color: "text-blue-600" }
        if (valorMensal <= 4000) return { status: "Moderado", color: "text-yellow-600" }
        return { status: "Alto Risco", color: "text-red-600" }
    }

    return (
        <div className="min-h-screen bg-background">
            {/* Header */}
            <header className="border-b bg-card">
                <div className="container mx-auto flex items-center justify-between px-4 py-4">
                    <div className="flex items-center gap-2">
                        <Car className="h-6 w-6 text-primary" />
                        <h1 className="text-xl font-bold">Sistema de Aluguel de Carros</h1>
                    </div>
                    <div className="flex items-center gap-4">
                        <div className="flex items-center gap-2">
                            <Building2 className="h-5 w-5 text-muted-foreground" />
                            <span className="text-sm font-medium">Agente - Banco XYZ</span>
                        </div>
                        <Button variant="ghost" size="sm">
                            <LogOut className="h-4 w-4 mr-2" />
                            Sair
                        </Button>
                    </div>
                </div>
            </header>

            {/* Main Content */}
            <main className="container mx-auto px-4 py-8">
                {/* Tabs */}
                <Tabs defaultValue="pedidos" className="space-y-6">
                    <TabsList>
                        <TabsTrigger value="pedidos">Avaliação de Pedidos</TabsTrigger>
                        <TabsTrigger value="veiculos">Gerenciar Veículos</TabsTrigger>
                    </TabsList>

                    {/* Pedidos Tab */}
                    <TabsContent value="pedidos" className="space-y-6">
                        <div className="mb-6">
                            <h2 className="text-3xl font-bold">Avaliação de Pedidos</h2>
                            <p className="text-muted-foreground">Analise e aprove pedidos de aluguel de automóveis</p>
                        </div>

                        {/* Estatísticas */}
                        <div className="grid gap-4 md:grid-cols-4 mb-6">
                            {["pendente", "em_analise", "aprovado", "rejeitado"].map(status => (
                                <Card key={status}>
                                    <CardHeader className="pb-2">
                                        <CardDescription>{statusLabels[status as PedidoStatus]}</CardDescription>
                                        <CardTitle className="text-3xl">{filterPedidos(status as PedidoStatus).length}</CardTitle>
                                    </CardHeader>
                                </Card>
                            ))}
                        </div>

                        {/* Lista de pedidos */}
                        <Tabs defaultValue="todos" className="space-y-4">
                            <TabsList>
                                <TabsTrigger value="todos">Todos ({localPedidos.length})</TabsTrigger>
                                {["pendente", "em_analise", "aprovado", "rejeitado"].map(status => (
                                    <TabsTrigger key={status} value={status}>
                                        {statusLabels[status as PedidoStatus]} ({filterPedidos(status as PedidoStatus).length})
                                    </TabsTrigger>
                                ))}
                            </TabsList>

                            {["todos", "pendente", "em_analise", "aprovado", "rejeitado"].map((tab) => (
                                <TabsContent key={tab} value={tab} className="space-y-4">
                                    {pedidosLoading ? (
                                        <div className="flex items-center justify-center py-8">
                                            <Loader2 className="h-8 w-8 animate-spin text-primary" />
                                            <span className="ml-2">Carregando pedidos...</span>
                                        </div>
                                    ) : (tab === "todos" ? localPedidos : filterPedidos(tab as PedidoStatus)).length === 0 ? (
                                        <Card>
                                            <CardContent className="flex flex-col items-center justify-center py-12">
                                                <p className="text-muted-foreground">Nenhum pedido encontrado</p>
                                            </CardContent>
                                        </Card>
                                    ) : (
                                        (tab === "todos" ? localPedidos : filterPedidos(tab as PedidoStatus)).map((pedido) => {
                                            const viabilidade = analisarViabilidade(pedido)
                                            return (
                                                <Card key={pedido.id}>
                                                    <CardHeader>
                                                        <div className="flex items-start justify-between">
                                                            <div className="flex-1">
                                                                <CardTitle className="flex items-center gap-2 mb-2">
                                                                    {pedido.automovel?.marca} {pedido.automovel?.modelo}
                                                                    <Badge className={statusColors[pedido.status as PedidoStatus]}>
                                                                        {statusLabels[pedido.status as PedidoStatus]}
                                                                    </Badge>
                                                                </CardTitle>
                                                                <CardDescription>
                                                                    {pedido.automovel?.ano} • Placa: {pedido.automovel?.placa}
                                                                </CardDescription>
                                                            </div>
                                                            <Button onClick={() => handleAvaliarPedido(pedido)}>
                                                                <Eye className="h-4 w-4 mr-2" />
                                                                Avaliar
                                                            </Button>
                                                        </div>
                                                    </CardHeader>
                                                    <CardContent>
                                                        <div className="space-y-4">
                                                            <div>
                                                                <h4 className="font-semibold mb-2">Informações do Cliente</h4>
                                                                <div className="grid grid-cols-2 md:grid-cols-4 gap-4 text-sm">
                                                                    <div><p className="text-muted-foreground">Nome</p><p className="font-medium">{pedido.clienteSolicitante?.nome || "N/A"}</p></div>
                                                                    <div><p className="text-muted-foreground">Email</p><p className="font-medium">{pedido.clienteSolicitante?.email || "N/A"}</p></div>
                                                                    <div><p className="text-muted-foreground">CPF</p><p className="font-medium">{pedido.clienteSolicitante?.cpf || "N/A"}</p></div>
                                                                    <div><p className="text-muted-foreground">Data Solicitação</p><p className="font-medium">{pedido.dataSolicitacao ? new Date(pedido.dataSolicitacao).toLocaleDateString() : "N/A"}</p></div>
                                                                </div>
                                                            </div>
                                                            <div>
                                                                <h4 className="font-semibold mb-2">Análise Financeira</h4>
                                                                <div className="grid grid-cols-2 md:grid-cols-4 gap-4 text-sm">
                                                                    <div><p className="text-muted-foreground">Valor Mensal</p><p className="font-medium">R$ {pedido.valorMensal?.toLocaleString("pt-BR", { minimumFractionDigits: 2 }) || pedido.automovel?.precoMensal.toLocaleString("pt-BR", { minimumFractionDigits: 2 }) || "N/A"}</p></div>
                                                                    <div><p className="text-muted-foreground">Período</p><p className="font-medium">{pedido.dataInicio ? new Date(pedido.dataInicio).toLocaleDateString() : "N/A"} - {pedido.dataFim ? new Date(pedido.dataFim).toLocaleDateString() : "N/A"}</p></div>
                                                                    <div><p className="text-muted-foreground">Viabilidade</p><p className={`font-medium ${viabilidade.color}`}>{viabilidade.status}</p></div>
                                                                    <div><p className="text-muted-foreground">Crédito Associado</p><p className="font-medium">{pedido.creditoAssociado ? "Sim" : "Não"}</p></div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </CardContent>
                                                </Card>
                                            )
                                        })
                                    )}
                                </TabsContent>
                            ))}
                        </Tabs>
                    </TabsContent>

                    {/* Veículos Tab */}
                    <TabsContent value="veiculos" className="space-y-6">
                        <div className="flex items-center justify-between mb-6">
                            <div>
                                <h2 className="text-3xl font-bold">Gerenciar Veículos</h2>
                                <p className="text-muted-foreground">Cadastre e gerencie os veículos disponíveis para aluguel</p>
                            </div>
                            <Button onClick={() => handleOpenVeiculoDialog()}>
                                <Plus className="h-4 w-4 mr-2" />
                                Cadastrar Veículo
                            </Button>
                        </div>

                        {veiculosLoading ? (
                            <div className="flex items-center justify-center py-8">
                                <Loader2 className="h-8 w-8 animate-spin text-primary" />
                                <span className="ml-2">Carregando veículos...</span>
                            </div>
                        ) : veiculos.length === 0 ? (
                            <Card>
                                <CardContent className="flex flex-col items-center justify-center py-12">
                                    <Car className="h-12 w-12 text-muted-foreground mb-4" />
                                    <p className="text-muted-foreground text-center">
                                        Nenhum veículo cadastrado ainda.
                                        <br />
                                        Clique em "Cadastrar Veículo" para adicionar o primeiro.
                                    </p>
                                </CardContent>
                            </Card>
                        ) : (
                            <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-3">
                                {veiculos.map((veiculo) => (
                                    <Card key={veiculo.matricula}>
                                        <CardHeader>
                                            <div className="flex items-start justify-between">
                                                <div>
                                                    <CardTitle className="mb-1">{veiculo.marca} {veiculo.modelo}</CardTitle>
                                                    <CardDescription>Ano {veiculo.ano}</CardDescription>
                                                </div>
                                                <div className="flex gap-2">
                                                    <Button variant="ghost" size="icon" onClick={() => handleOpenVeiculoDialog(veiculo)}>
                                                        <Edit className="h-4 w-4" />
                                                    </Button>
                                                    <Button variant="ghost" size="icon" onClick={() => handleDeleteVeiculo(veiculo.matricula)}>
                                                        <Trash2 className="h-4 w-4" />
                                                    </Button>
                                                </div>
                                            </div>
                                        </CardHeader>
                                        <CardContent>
                                            <div className="space-y-2 text-sm">
                                                <div className="flex justify-between"><span className="text-muted-foreground">Matrícula:</span><span className="font-medium">{veiculo.matricula}</span></div>
                                                <div className="flex justify-between"><span className="text-muted-foreground">Placa:</span><span className="font-medium">{veiculo.placa}</span></div>
                                                <div className="flex justify-between"><span className="text-muted-foreground">Valor Mensal:</span><span className="font-medium">R$ {(veiculo.valorMensal || 0).toLocaleString("pt-BR", { minimumFractionDigits: 2 })}</span></div>
                                            </div>
                                        </CardContent>
                                    </Card>
                                ))}
                            </div>
                        )}
                    </TabsContent>
                </Tabs>

                {/* Dialog Avaliação */}
                <Dialog open={isDialogOpen} onOpenChange={setIsDialogOpen}>
                    <DialogContent className="max-w-2xl">
                        <DialogHeader>
                            <DialogTitle>Avaliar Pedido</DialogTitle>
                            <DialogDescription>Modifique o status do pedido e adicione observações, se necessário</DialogDescription>
                        </DialogHeader>
                        {selectedPedido && (
                            <div className="space-y-4 py-4">
                                <div className="grid grid-cols-2 gap-4 p-4 bg-muted rounded-lg">
                                    <div><p className="text-sm text-muted-foreground">Cliente</p><p className="font-medium">{selectedPedido.clienteSolicitante?.nome || "N/A"}</p></div>
                                    <div><p className="text-sm text-muted-foreground">Veículo</p><p className="font-medium">{selectedPedido.automovel?.marca} {selectedPedido.automovel?.modelo}</p></div>
                                    <div><p className="text-sm text-muted-foreground">Valor Mensal</p><p className="font-medium">R$ {selectedPedido.valorMensal?.toLocaleString("pt-BR", { minimumFractionDigits: 2 }) || "N/A"}</p></div>
                                    <div><p className="text-sm text-muted-foreground">Viabilidade</p><p className={`font-medium ${analisarViabilidade(selectedPedido).color}`}>{analisarViabilidade(selectedPedido).status}</p></div>
                                </div>

                                <div className="space-y-2">
                                    <Label htmlFor="status">Modificar Status</Label>
                                    <Select value={novoStatus} onValueChange={(value) => setNovoStatus(value as PedidoStatus)}>
                                        <SelectTrigger id="status"><SelectValue /></SelectTrigger>
                                        <SelectContent>
                                            {["pendente", "em_analise", "aprovado", "rejeitado"].map(s => <SelectItem key={s} value={s}>{statusLabels[s as PedidoStatus]}</SelectItem>)}
                                        </SelectContent>
                                    </Select>
                                </div>

                                <div className="space-y-2">
                                    <Label htmlFor="observacoes">Observações</Label>
                                    <Textarea id="observacoes" placeholder="Adicione observações..." value={observacoes} onChange={(e) => setObservacoes(e.target.value)} rows={4} />
                                </div>
                            </div>
                        )}
                        <DialogFooter className="gap-2">
                            <Button variant="outline" onClick={() => setIsDialogOpen(false)}>Cancelar</Button>
                            {selectedPedido?.status === "pendente" && (
                                <Button variant="secondary" onClick={handleColocarEmAnalise}>
                                    Colocar em Análise
                                </Button>
                            )}
                            <Button variant="destructive" onClick={handleRejeitar}>
                                <XCircle className="h-4 w-4 mr-2" />
                                Rejeitar
                            </Button>
                            <Button onClick={handleAprovar}>
                                <CheckCircle className="h-4 w-4 mr-2" />
                                Aprovar
                            </Button>
                            <Button variant="secondary" onClick={handleModificarStatus}>
                                Modificar Status
                            </Button>
                        </DialogFooter>
                    </DialogContent>
                </Dialog>

                {/* Dialog Veículo */}
                <Dialog open={isVeiculoDialogOpen} onOpenChange={setIsVeiculoDialogOpen}>
                    <DialogContent>
                        <DialogHeader>
                            <DialogTitle>{editingVeiculo ? "Editar Veículo" : "Cadastrar Novo Veículo"}</DialogTitle>
                            <DialogDescription>Preencha as informações do veículo para {editingVeiculo ? "atualizar" : "cadastrar"} no sistema</DialogDescription>
                        </DialogHeader>
                        <div className="space-y-4 py-4">
                            <div className="space-y-2">
                                <Label htmlFor="matricula">Matrícula</Label>
                                <Input id="matricula" placeholder="VEI001" value={veiculoForm.matricula} onChange={(e) => setVeiculoForm({ ...veiculoForm, matricula: e.target.value })} disabled={!!editingVeiculo} />
                            </div>
                            <div className="grid grid-cols-2 gap-4">
                                <div className="space-y-2">
                                    <Label htmlFor="marca">Marca</Label>
                                    <Input id="marca" placeholder="Toyota" value={veiculoForm.marca} onChange={(e) => setVeiculoForm({ ...veiculoForm, marca: e.target.value })} />
                                </div>
                                <div className="space-y-2">
                                    <Label htmlFor="modelo">Modelo</Label>
                                    <Input id="modelo" placeholder="Corolla" value={veiculoForm.modelo} onChange={(e) => setVeiculoForm({ ...veiculoForm, modelo: e.target.value })} />
                                </div>
                            </div>
                            <div className="grid grid-cols-2 gap-4">
                                <div className="space-y-2">
                                    <Label htmlFor="ano">Ano</Label>
                                    <Input id="ano" type="number" placeholder="2024" value={veiculoForm.ano} onChange={(e) => setVeiculoForm({ ...veiculoForm, ano: Number.parseInt(e.target.value) || 0 })} />
                                </div>
                                <div className="space-y-2">
                                    <Label htmlFor="placa">Placa</Label>
                                    <Input id="placa" placeholder="ABC-1234" value={veiculoForm.placa} onChange={(e) => setVeiculoForm({ ...veiculoForm, placa: e.target.value.toUpperCase() })} />
                                </div>
                            </div>
                            <div className="space-y-2">
                                <Label htmlFor="valorMensal">Valor Mensal (R$)</Label>
                                <Input
                                    id="valorMensal"
                                    type="number"
                                    step="0.01"
                                    placeholder="2500.00"
                                    value={veiculoForm.valorMensal || 0}
                                    onChange={(e) => setVeiculoForm({ ...veiculoForm, valorMensal: Number.parseFloat(e.target.value) || 0 })}
                                />
                            </div>
                        </div>
                        <DialogFooter>
                            <Button variant="outline" onClick={() => setIsVeiculoDialogOpen(false)} disabled={veiculosLoading}>Cancelar</Button>
                            <Button onClick={handleSaveVeiculo} disabled={veiculosLoading}>{veiculosLoading && <Loader2 className="h-4 w-4 mr-2 animate-spin" />}{editingVeiculo ? "Atualizar" : "Cadastrar"}</Button>
                        </DialogFooter>
                    </DialogContent>
                </Dialog>
            </main>
        </div>
    )
}