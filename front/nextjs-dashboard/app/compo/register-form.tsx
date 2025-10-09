"use client"

import { useState } from "react"
import Link from "next/link"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle } from "@/components/ui/card"
import { RadioGroup, RadioGroupItem } from "@/components/ui/radio-group"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { Dialog, DialogContent, DialogDescription, DialogFooter, DialogHeader, DialogTitle } from "@/components/ui/dialog"
import { AgentType, RegisterData, UserType } from "../lib/definitions"
import { RegisterApi } from "../lib/data"

export function RegisterForm() {
    const [userType, setUserType] = useState<UserType>("cliente")
    const [name, setName] = useState("")
    const [email, setEmail] = useState("")
    const [senha, setsenha] = useState("")
    const [confirmsenha, setConfirmsenha] = useState("")
    const [cpf, setCpf] = useState("")
    const [registro, setRegistro] = useState("")
    const [profissao, setProfissao] = useState("")
    const [agentType, setAgentType] = useState<AgentType>("empresa")
    const [isLoading, setIsLoading] = useState(false)
    const [modalSuccessOpen, setModalSuccessOpen] = useState(false)
    const [modalErrorOpen, setModalErrorOpen] = useState(false)
    const [errorMessage, setErrorMessage] = useState("")

    const formatCPF = (value: string) => {
        const numbers = value.replace(/\D/g, "")
        if (numbers.length <= 11) {
            return numbers
                .replace(/(\d{3})(\d)/, "$1.$2")
                .replace(/(\d{3})(\d)/, "$1.$2")
                .replace(/(\d{3})(\d{1,2})$/, "$1-$2")
        }
        return value
    }

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault()

        if (senha !== confirmsenha) {
            setErrorMessage("As senhas não coincidem")
            setModalErrorOpen(true)
            return
        }

        setIsLoading(true)

        let userData: RegisterData
        if (userType === "cliente") {
            userData = {
                userType: "cliente",
                nome: name,
                email,
                senha,
                cpf,
                registro,
                profissao,
            }
        } else {
            userData = {
                userType: "agente",
                nome: name,
                email,
                senha,
                agentType,
            }
        }

        try {
            await RegisterApi(userData)
            setModalSuccessOpen(true)
            setName("")
            setEmail("")
            setsenha("")
            setConfirmsenha("")
            setCpf("")
            setRegistro("")
            setProfissao("")
            setAgentType("empresa")
        } catch (error: any) {
            setErrorMessage(error.message || "Erro ao registrar usuário")
            setModalErrorOpen(true)
        } finally {
            setIsLoading(false)
        }
    }

    return (
        <>
            <Card className="w-full max-w-md">
                <CardHeader className="space-y-1">
                    <CardTitle className="text-2xl font-bold">Criar conta</CardTitle>
                    <CardDescription>Preencha os dados abaixo para criar sua conta</CardDescription>
                </CardHeader>
                <form onSubmit={handleSubmit}>
                    <CardContent className="space-y-4">
                        <div className="space-y-3">
                            <Label>Tipo de usuário</Label>
                            <RadioGroup value={userType} onValueChange={(value) => setUserType(value as UserType)}>
                                <div className="flex items-center space-x-2">
                                    <RadioGroupItem value="cliente" id="cliente" />
                                    <Label htmlFor="cliente" className="font-normal cursor-pointer">Cliente</Label>
                                </div>
                                <div className="flex items-center space-x-2">
                                    <RadioGroupItem value="agente" id="agente" />
                                    <Label htmlFor="agente" className="font-normal cursor-pointer">Agente</Label>
                                </div>
                            </RadioGroup>
                        </div>

                        <div className="space-y-2">
                            <Label htmlFor="name">Nome completo</Label>
                            <Input
                                id="name"
                                type="text"
                                placeholder="João Silva"
                                value={name}
                                maxLength={30}
                                onChange={(e) => setName(e.target.value)}
                                required
                            />
                            <p className="text-xs text-muted-foreground">Máximo 30 caracteres</p>
                        </div>

                        <div className="space-y-2">
                            <Label htmlFor="email">Email</Label>
                            <Input
                                id="email"
                                type="email"
                                placeholder="seu@email.com"
                                value={email}
                                onChange={(e) => setEmail(e.target.value)}
                                required
                            />
                        </div>

                        {userType === "cliente" && (
                            <>
                                <div className="space-y-2">
                                    <Label htmlFor="cpf">CPF</Label>
                                    <Input
                                        id="cpf"
                                        type="text"
                                        placeholder="000.000.000-00"
                                        value={cpf}
                                        maxLength={14}
                                        onChange={(e) => setCpf(formatCPF(e.target.value))}
                                        required
                                    />
                                    <p className="text-xs text-muted-foreground">Formato: 000.000.000-00</p>
                                </div>

                                <div className="space-y-2">
                                    <Label htmlFor="registro">Registro</Label>
                                    <Input
                                        id="registro"
                                        type="text"
                                        placeholder="Número de registro"
                                        value={registro}
                                        maxLength={30}
                                        onChange={(e) => setRegistro(e.target.value)}
                                        required
                                    />
                                    <p className="text-xs text-muted-foreground">Máximo 30 caracteres</p>
                                </div>

                                <div className="space-y-2">
                                    <Label htmlFor="profissao">Profissão</Label>
                                    <Input
                                        id="profissao"
                                        type="text"
                                        placeholder="Sua profissão"
                                        value={profissao}
                                        maxLength={30}
                                        onChange={(e) => setProfissao(e.target.value)}
                                        required
                                    />
                                    <p className="text-xs text-muted-foreground">Máximo 30 caracteres</p>
                                </div>
                            </>
                        )}

                        {userType === "agente" && (
                            <div className="space-y-2">
                                <Label htmlFor="agent-type">Tipo de agente</Label>
                                <Select value={agentType} onValueChange={(value) => setAgentType(value as AgentType)}>
                                    <SelectTrigger id="agent-type">
                                        <SelectValue placeholder="Selecione o tipo" />
                                    </SelectTrigger>
                                    <SelectContent>
                                        <SelectItem value="empresa">Empresa</SelectItem>
                                        <SelectItem value="banco">Banco</SelectItem>
                                    </SelectContent>
                                </Select>
                            </div>
                        )}

                        <div className="space-y-2 pt-10">
                            <Label htmlFor="senha">Senha</Label>
                            <Input
                                id="senha"
                                type="password"
                                placeholder="••••••••"
                                value={senha}
                                minLength={8}
                                onChange={(e) => setsenha(e.target.value)}
                                required
                            />
                            <p className="text-xs text-muted-foreground">Mínimo 8 caracteres</p>
                        </div>

                        <div className="space-y-2">
                            <Label htmlFor="confirm-senha">Confirmar senha</Label>
                            <Input
                                id="confirm-senha"
                                type="password"
                                placeholder="••••••••"
                                value={confirmsenha}
                                minLength={8}
                                onChange={(e) => setConfirmsenha(e.target.value)}
                                required
                            />
                        </div>
                    </CardContent>

                    <CardFooter className="flex flex-col space-y-4">
                        <Button type="submit" className="w-full" disabled={isLoading}>
                            {isLoading ? "Criando conta..." : "Criar conta"}
                        </Button>
                        <p className="text-sm text-center text-muted-foreground">
                            Já tem uma conta?{" "}
                            <Link href="/login" className="text-primary hover:underline font-medium">
                                Entrar
                            </Link>
                        </p>
                    </CardFooter>
                </form>
            </Card>

            {/* Modal de sucesso */}
            <Dialog open={modalSuccessOpen} onOpenChange={setModalSuccessOpen}>
                <DialogContent>
                    <DialogHeader>
                        <DialogTitle>Sucesso!</DialogTitle>
                        <DialogDescription>Conta criada com sucesso.</DialogDescription>
                    </DialogHeader>
                    <DialogFooter>
                        <Button onClick={() => setModalSuccessOpen(false)}>Fechar</Button>
                    </DialogFooter>
                </DialogContent>
            </Dialog>

            {/* Modal de erro */}
            <Dialog open={modalErrorOpen} onOpenChange={setModalErrorOpen}>
                <DialogContent>
                    <DialogHeader>
                        <DialogTitle>Erro</DialogTitle>
                        <DialogDescription>{errorMessage}</DialogDescription>
                    </DialogHeader>
                    <DialogFooter>
                        <Button onClick={() => setModalErrorOpen(false)}>Fechar</Button>
                    </DialogFooter>
                </DialogContent>
            </Dialog>
        </>
    )
}
