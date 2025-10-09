import { useState, useEffect } from "react";
import {
  listarTodosVeiculos,
  criarVeiculo,
  atualizarVeiculo,
  deletarVeiculo,
  Veiculo,
  VeiculoDto,
} from "@/app/lib/data"; // Ajuste o caminho conforme necessário

export function useVeiculos() {
  const [veiculos, setVeiculos] = useState<Veiculo[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const fetchVeiculos = async () => {
    setIsLoading(true);
    try {
      const data = await listarTodosVeiculos();
      console.log("Dados retornados por listarTodosVeiculos:", data); // Log para depuração
      // Mapeia precoMensal para valorMensal
      const mappedVeiculos = data.map((v: any) => ({
        matricula: v.matricula,
        ano: v.ano,
        marca: v.marca,
        modelo: v.modelo,
        placa: v.placa,
        valorMensal: v.precoMensal ? Number(v.precoMensal) : 0, // Converte precoMensal para valorMensal
      }));
      setVeiculos(mappedVeiculos);
    } catch (err) {
      setError("Erro ao carregar veículos");
      console.error("Erro ao carregar veículos:", err);
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    fetchVeiculos();
  }, []);

  const criarNovoVeiculo = async (veiculoData: VeiculoDto) => {
    setIsLoading(true);
    try {
      const newVeiculo = await criarVeiculo(veiculoData);
      await fetchVeiculos(); // Força recarregamento completo da lista
      return true;
    } catch (err) {
      setError("Erro ao criar veículo");
      console.error("Erro ao criar veículo:", err);
      return false;
    } finally {
      setIsLoading(false);
    }
  };

  const atualizarVeiculoExistente = async (matricula: number, veiculoData: VeiculoDto) => {
    setIsLoading(true);
    try {
      const updatedVeiculo = await atualizarVeiculo({ ...veiculoData, matricula });
      await fetchVeiculos(); // Força recarregamento completo da lista
      return true;
    } catch (err) {
      setError("Erro ao atualizar veículo");
      console.error("Erro ao atualizar veículo:", err);
      return false;
    } finally {
      setIsLoading(false);
    }
  };

  const removerVeiculo = async (matricula: number) => {
    setIsLoading(true);
    try {
      await deletarVeiculo(matricula);
      await fetchVeiculos(); // Força recarregamento completo da lista
      return true;
    } catch (err) {
      setError("Erro ao remover veículo");
      console.error("Erro ao remover veículo:", err);
      return false;
    } finally {
      setIsLoading(false);
    }
  };

  const refetch = () => fetchVeiculos();

  return { veiculos, isLoading, error, criarNovoVeiculo, atualizarVeiculoExistente, removerVeiculo, refetch };
}