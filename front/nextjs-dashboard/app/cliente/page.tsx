"use client"

import { useState, useEffect } from "react"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardDescription, CardHeader, CardTitle, CardFooter } from "@/components/ui/card"
import { Dialog, DialogContent, DialogFooter, DialogHeader, DialogTitle, DialogTrigger } from "@/components/ui/dialog"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { Car, Loader2, LogOut, User } from "lucide-react"
import { useToast } from "@/hooks/use-toast"
import { VeiculoDto } from "@/app/lib/data" // Import VeiculoDto interface

interface Automovel {
    id: string
    marca: string
    modelo: string
    ano?: number
    placa: string
    precoMensal?: number
}

interface PedidoAluguelDto {
    placaVeiculo: string
    dataInicio: string
    dataFim: string
    valorMensal: number
    emailCliente: string
}

export default function ClienteHomePage() {
    const { toast } = useToast()

    const [automoveis, setAutomoveis] = useState<Automovel[]>([])
    const [isLoading, setIsLoading] = useState(true)
    const [error, setError] = useState<string | null>(null)
    const [token, setToken] = useState<string | null>(null)
    const [user, setUser] = useState<{ email: string } | null>(null)

    const [selectedCar, setSelectedCar] = useState<Automovel | null>(null)
    const [isDialogOpen, setIsDialogOpen] = useState(false)
    const [formData, setFormData] = useState({
        dataInicio: "",
        dataFim: "",
        valorMensal: "",
        creditoAssociado: "false",
    })

    // Estado para cadastro de empregador
    const [isEmpregadorDialogOpen, setIsEmpregadorDialogOpen] = useState(false)
    const [empregadorData, setEmpregadorData] = useState({
        nome: "",
        cpf: "",
        emailCliente: "",
        rendimento: "",
    })

    // Estado para cadastro de veículo
    const [isVeiculoDialogOpen, setIsVeiculoDialogOpen] = useState(false)
    const [veiculoForm, setVeiculoForm] = useState<VeiculoDto>({
        ano: new Date().getFullYear(),
        marca: "",
        modelo: "",
        placa: "",
        valorMensal: 0,
    })

    // Recupera user e token do localStorage no client
    useEffect(() => {
        const storedUser = localStorage.getItem("auth_user")
        const storedToken = localStorage.getItem("auth_token")
        if (storedUser) setUser(JSON.parse(storedUser))
        if (storedToken) setToken(storedToken)

        if (storedUser) setEmpregadorData(prev => ({ ...prev, emailCliente: JSON.parse(storedUser).email }))
    }, [])

    // Fetch dos carros disponíveis
    useEffect(() => {
        fetch("http://localhost:8081/api/v1/automovel")
            .then(res => res.json())
            .then(data => setAutomoveis(data))
            .catch(() => setError("Não foi possível carregar automóveis"))
            .finally(() => setIsLoading(false))
    }, [])

    // Exibe toast de erro
    useEffect(() => {
        if (error) {
            toast({
                title: "Erro",
                description: error,
                variant: "destructive",
            })
        }
    }, [error, toast])

    const resetForm = () => {
        setFormData({
            dataInicio: "",
            dataFim: "",
            valorMensal: "",
            creditoAssociado: "false",
        })
        setSelectedCar(null)
    }

    // Criação de pedido de aluguel
    const handleCreatePedido = async () => {
        if (!selectedCar || !user) return

        if (!formData.dataInicio || !formData.dataFim) {
            toast({
                title: "Erro",
                description: "Preencha as datas de início e fim",
                variant: "destructive",
            })
            return
        }

        const pedidoDto: PedidoAluguelDto = {
            placaVeiculo: selectedCar.placa,
            dataInicio: formData.dataInicio,
            dataFim: formData.dataFim,
            valorMensal: parseFloat(formData.valorMensal) || selectedCar.precoMensal || 0,
            emailCliente: user.email
        }

        try {
            const res = await fetch("http://localhost:8081/api/v1/pedido", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(pedidoDto),
            })

            const errorText = await res.text()

            if (res.ok) {
                toast({
                    title: "Sucesso",
                    description: "Pedido criado com sucesso!",
                })
                resetForm()
                setIsDialogOpen(false)
            } else {
                toast({
                    title: "Erro",
                    description: errorText || `Não foi possível criar o pedido. Status ${res.status}`,
                    variant: "destructive",
                })
            }
        } catch (err) {
            console.error(err)
            toast({
                title: "Erro",
                description: "Não foi possível criar o pedido. Tente novamente.",
                variant: "destructive",
            })
        }
    }

    // Criação de empregador
    const handleCreateEmpregador = async () => {
        if (!empregadorData.nome || !empregadorData.cpf || !empregadorData.emailCliente || !empregadorData.rendimento) {
            toast({
                title: "Erro",
                description: "Preencha todos os campos do empregador",
                variant: "destructive",
            })
            return
        }

        if (!token) {
            toast({
                title: "Erro",
                description: "Usuário não autenticado",
                variant: "destructive",
            })
            return
        }

        try {
            const payload = {
                ...empregadorData,
                rendimento: parseFloat(empregadorData.rendimento),
            }

            const res = await fetch("http://localhost:8081/api/v1/empregador", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${token}`
                },
                body: JSON.stringify(payload),
            })

            if (res.ok) {
                toast({
                    title: "Sucesso",
                    description: "Empregador cadastrado com sucesso!",
                })
                setEmpregadorData({ nome: "", cpf: "", emailCliente: user?.email || "", rendimento: "" })
                setIsEmpregadorDialogOpen(false)
            } else {
                const errorText = await res.text()
                toast({
                    title: "Erro",
                    description: errorText || "Não foi possível cadastrar o empregador.",
                    variant: "destructive",
                })
            }
        } catch (err) {
            console.error(err)
            toast({
                title: "Erro",
                description: "Falha na conexão com o servidor.",
                variant: "destructive",
            })
        }
    }

    // Criação de veículo
    const handleCreateVeiculo = async () => {
        if (!token) {
            toast({
                title: "Erro",
                description: "Usuário não autenticado",
                variant: "destructive",
            })
            return
        }

        if (!veiculoForm.marca || !veiculoForm.modelo || !veiculoForm.placa || !veiculoForm.valorMensal) {
            toast({
                title: "Erro",
                description: "Preencha todos os campos do veículo",
                variant: "destructive",
            })
            return
        }

        try {
            const veiculoDto: VeiculoDto = {
                ano: veiculoForm.ano,
                marca: veiculoForm.marca,
                modelo: veiculoForm.modelo,
                placa: veiculoForm.placa,
                valorMensal: veiculoForm.valorMensal,
            }

            const res = await fetch("http://localhost:8081/api/v1/automovel", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${token}`
                },
                body: JSON.stringify(veiculoDto),
            })

            if (res.ok) {
                toast({
                    title: "Sucesso",
                    description: "Veículo cadastrado com sucesso!",
                })
                setVeiculoForm({
                    ano: new Date().getFullYear(),
                    marca: "",
                    modelo: "",
                    placa: "",
                    valorMensal: 0,
                })
                setIsVeiculoDialogOpen(false)
                // Refresh the automoveis list
                const updatedAutomoveis = await fetch("http://localhost:8081/api/v1/automovel").then(res => res.json())
                setAutomoveis(updatedAutomoveis)
            } else {
                const errorText = await res.text()
                toast({
                    title: "Erro",
                    description: errorText || "Não foi possível cadastrar o veículo.",
                    variant: "destructive",
                })
            }
        } catch (err) {
            console.error(err)
            toast({
                title: "Erro",
                description: "Falha na conexão com o servidor.",
                variant: "destructive",
            })
        }
    }

    if (isLoading) {
        return (
            <div className="min-h-screen bg-background flex items-center justify-center">
                <Loader2 className="h-8 w-8 animate-spin text-primary" />
            </div>
        )
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
                            <User className="h-5 w-5 text-muted-foreground" />
                            <span className="text-sm font-medium">Cliente</span>
                        </div>
                        <Button variant="ghost" size="sm" onClick={() => (window.location.href = "/login")}>
                            <LogOut className="h-4 w-4 mr-2" />
                            Sair
                        </Button>
                    </div>
                </div>
            </header>

            {/* Main Content */}
            <main className="container mx-auto px-4 py-8">
                <div className="mb-6 flex items-center justify-between">
                    <div>
                        <h2 className="text-3xl font-bold">Automóveis Disponíveis</h2>
                        <p className="text-muted-foreground">Escolha um carro e faça uma proposta de aluguel</p>
                    </div>

                    {/* Botões para modais de cadastro */}
                    <div className="space-x-2">
                        <Dialog open={isEmpregadorDialogOpen} onOpenChange={setIsEmpregadorDialogOpen}>
                            <DialogTrigger asChild>
                                <Button>Cadastrar Empregador</Button>
                            </DialogTrigger>
                            <DialogContent className="max-w-md">
                                <DialogHeader>
                                    <DialogTitle>Novo Empregador</DialogTitle>
                                </DialogHeader>
                                <div className="grid gap-4 py-4">
                                    <div className="space-y-2">
                                        <Label htmlFor="nome">Nome</Label>
                                        <Input
                                            id="nome"
                                            value={empregadorData.nome}
                                            onChange={(e) => setEmpregadorData({ ...empregadorData, nome: e.target.value })}
                                        />
                                    </div>
                                    <div className="space-y-2">
                                        <Label htmlFor="cpf">CPF</Label>
                                        <Input
                                            id="cpf"
                                            value={empregadorData.cpf}
                                            onChange={(e) => setEmpregadorData({ ...empregadorData, cpf: e.target.value })}
                                        />
                                    </div>
                                    <div className="space-y-2">
                                        <Label htmlFor="emailCliente">Email do Cliente</Label>
                                        <Input
                                            id="emailCliente"
                                            type="email"
                                            value={empregadorData.emailCliente}
                                            readOnly
                                        />
                                    </div>
                                    <div className="space-y-2">
                                        <Label htmlFor="rendimento">Rendimento (R$)</Label>
                                        <Input
                                            id="rendimento"
                                            type="number"
                                            step="0.01"
                                            value={empregadorData.rendimento}
                                            onChange={(e) => setEmpregadorData({ ...empregadorData, rendimento: e.target.value })}
                                        />
                                    </div>
                                </div>
                                <DialogFooter>
                                    <Button variant="outline" onClick={() => setIsEmpregadorDialogOpen(false)}>Cancelar</Button>
                                    <Button onClick={handleCreateEmpregador}>Cadastrar</Button>
                                </DialogFooter>
                            </DialogContent>
                        </Dialog>
                        <Dialog open={isVeiculoDialogOpen} onOpenChange={setIsVeiculoDialogOpen}>
                            <DialogTrigger asChild>
                                <Button>Cadastrar Veículo</Button>
                            </DialogTrigger>
                            <DialogContent className="max-w-md">
                                <DialogHeader>
                                    <DialogTitle>Novo Veículo</DialogTitle>
                                </DialogHeader>
                                <div className="grid gap-4 py-4">
                                    <div className="space-y-2">
                                        <Label htmlFor="marca">Marca</Label>
                                        <Input
                                            id="marca"
                                            value={veiculoForm.marca}
                                            onChange={(e) => setVeiculoForm({ ...veiculoForm, marca: e.target.value })}
                                        />
                                    </div>
                                    <div className="space-y-2">
                                        <Label htmlFor="modelo">Modelo</Label>
                                        <Input
                                            id="modelo"
                                            value={veiculoForm.modelo}
                                            onChange={(e) => setVeiculoForm({ ...veiculoForm, modelo: e.target.value })}
                                        />
                                    </div>
                                    <div className="space-y-2">
                                        <Label htmlFor="ano">Ano</Label>
                                        <Input
                                            id="ano"
                                            type="number"
                                            value={veiculoForm.ano}
                                            onChange={(e) => setVeiculoForm({ ...veiculoForm, ano: parseInt(e.target.value) || new Date().getFullYear() })}
                                        />
                                    </div>
                                    <div className="space-y-2">
                                        <Label htmlFor="placa">Placa</Label>
                                        <Input
                                            id="placa"
                                            value={veiculoForm.placa}
                                            onChange={(e) => setVeiculoForm({ ...veiculoForm, placa: e.target.value.toUpperCase() })}
                                        />
                                    </div>
                                    <div className="space-y-2">
                                        <Label htmlFor="valorMensal">Valor Mensal (R$)</Label>
                                        <Input
                                            id="valorMensal"
                                            type="number"
                                            step="0.01"
                                            value={veiculoForm.valorMensal}
                                            onChange={(e) => setVeiculoForm({ ...veiculoForm, valorMensal: parseFloat(e.target.value) || 0 })}
                                        />
                                    </div>
                                </div>
                                <DialogFooter>
                                    <Button variant="outline" onClick={() => setIsVeiculoDialogOpen(false)}>Cancelar</Button>
                                    <Button onClick={handleCreateVeiculo}>Cadastrar</Button>
                                </DialogFooter>
                            </DialogContent>
                        </Dialog>
                    </div>
                </div>

                <div className="grid grid-cols-3 gap-4">
                    {automoveis.length === 0 ? (
                        <Card key="empty">
                            <CardContent className="flex flex-col items-center justify-center py-12">
                                <Car className="h-12 w-12 text-muted-foreground mb-4" />
                                <p className="text-muted-foreground">Nenhum automóvel disponível</p>
                            </CardContent>
                        </Card>
                    ) : (
                        automoveis.map((car) => (
                            <Card key={car.placa}>
                                <CardHeader>
                                    <CardTitle>{car.marca} {car.modelo}</CardTitle>
                                    <CardDescription>{car.ano ?? "-"} • Placa: {car.placa ?? "-"}</CardDescription>
                                </CardHeader>
                                <CardContent>
                                    <p>Preço Mensal: R$ {(car.precoMensal ?? 0).toFixed(2)}</p>
                                </CardContent>
                                <CardFooter>
                                    <Dialog
                                        open={selectedCar?.id === car.id && isDialogOpen}
                                        onOpenChange={(open) => { setIsDialogOpen(open); if (!open) resetForm() }}
                                    >
                                        <DialogTrigger asChild>
                                            <Button
                                                onClick={() => {
                                                    setSelectedCar(car)
                                                    setFormData({
                                                        dataInicio: "",
                                                        dataFim: "",
                                                        valorMensal: (car.precoMensal ?? 0).toString(),
                                                        creditoAssociado: "false",
                                                    })
                                                    setIsDialogOpen(true)
                                                }}
                                            >
                                                Fazer Pedido
                                            </Button>
                                        </DialogTrigger>
                                        <DialogContent className="max-w-2xl max-h-[90vh] overflow-y-auto">
                                            <DialogHeader>
                                                <DialogTitle>Novo Pedido de Aluguel</DialogTitle>
                                            </DialogHeader>
                                            <div className="grid gap-4 py-4">
                                                <div className="grid grid-cols-2 gap-4">
                                                    <div className="space-y-2">
                                                        <Label htmlFor="dataInicio">Data Início</Label>
                                                        <Input
                                                            id="dataInicio"
                                                            type="date"
                                                            value={formData.dataInicio}
                                                            onChange={(e) => setFormData({ ...formData, dataInicio: e.target.value })}
                                                        />
                                                    </div>
                                                    <div className="space-y-2">
                                                        <Label htmlFor="dataFim">Data Fim</Label>
                                                        <Input
                                                            id="dataFim"
                                                            type="date"
                                                            value={formData.dataFim}
                                                            onChange={(e) => setFormData({ ...formData, dataFim: e.target.value })}
                                                        />
                                                    </div>
                                                </div>
                                                <div className="grid grid-cols-2 gap-4">
                                                    <div className="space-y-2">
                                                        <Label htmlFor="valorMensal">Valor Mensal (R$)</Label>
                                                        <Input
                                                            id="valorMensal"
                                                            type="number"
                                                            step="0.01"
                                                            value={formData.valorMensal}
                                                            onChange={(e) => setFormData({ ...formData, valorMensal: e.target.value })}
                                                        />
                                                    </div>
                                                    <div className="space-y-2">
                                                        <Label htmlFor="creditoAssociado">Crédito Associado</Label>
                                                        <Select
                                                            value={formData.creditoAssociado}
                                                            onValueChange={(value) => setFormData({ ...formData, creditoAssociado: value })}
                                                        >
                                                            <SelectTrigger id="creditoAssociado">
                                                                <SelectValue />
                                                            </SelectTrigger>
                                                            <SelectContent>
                                                                <SelectItem key="false" value="false">Não</SelectItem>
                                                                <SelectItem key="true" value="true">Sim</SelectItem>
                                                            </SelectContent>
                                                        </Select>
                                                    </div>
                                                </div>
                                            </div>
                                            <DialogFooter>
                                                <Button variant="outline" onClick={() => { setIsDialogOpen(false); resetForm() }}>Cancelar</Button>
                                                <Button onClick={handleCreatePedido}>Criar Pedido</Button>
                                            </DialogFooter>
                                        </DialogContent>
                                    </Dialog>
                                </CardFooter>
                            </Card>
                        ))
                    )}
                </div>
            </main>
        </div>
    )
}