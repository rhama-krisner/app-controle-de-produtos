package com.example.controledeprodutos;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class ProdutoDAO { //Data Assets Objects
    private final SQLiteDatabase write;
    private final SQLiteDatabase read;

    public ProdutoDAO(Context context) {
        DBHelper dbHelper = new DBHelper(context);
        this.write = dbHelper.getWritableDatabase();
        this.read = dbHelper.getReadableDatabase();
    }

    @SuppressLint("Range")
    public List<Produto> getListProdutos() {
        List<Produto> produtoList = new ArrayList<>();

        String sql = " SELECT * FROM " + DBHelper.TB_PRODUTO + ";";
        Cursor cursor = read.rawQuery(sql, null);

        while (cursor.moveToNext()) {
            cursor.getInt(cursor.getColumnIndex("id"));
            String nome = cursor.getString(cursor.getColumnIndex("nome"));
            int estoque = cursor.getInt(cursor.getColumnIndex("estoque"));
            double valor = cursor.getDouble(cursor.getColumnIndex("valor"));

            Produto produto = new Produto();
            produto.setNome(nome);
            produto.setEstoque(estoque);
            produto.setValor(valor);

            produtoList.add(produto);
        }

        return produtoList;
    }

    public void salvarProduto(Produto produto) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("nome", produto.getNome());
        contentValues.put("estoque", produto.getEstoque());
        contentValues.put("valor", produto.getValor());

        try {
            write.insert(DBHelper.TB_PRODUTO, null, contentValues);

        } catch (Exception e) {
            Log.i("ERROR", "Erro ao salvar produto: " + e.getMessage());
        }
    }

    public void atualizaProduto(Produto produto) {
        ContentValues cv = new ContentValues();
        cv.put("nome", produto.getNome());
        cv.put("estoque", produto.getEstoque());
        cv.put("valor", produto.getValor());

        String where = "id=?";
        String[] args = {String.valueOf(produto.getId())};
        try {
            write.update(DBHelper.TB_PRODUTO, cv, where, args);
            //write.close();
        } catch (Exception e) {
            Log.i("ERROR", "Erro ao atualizar produto" + e.getMessage());
        }

    }

    public void deleteProduto(Produto produto) {
        String[] args = {String.valueOf(produto.getId())};
        String where = "id=?";
        write.delete(DBHelper.TB_PRODUTO, where, args);
//      write.close();
    }

}
