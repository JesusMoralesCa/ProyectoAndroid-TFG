package com.example.tfg.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tfg.R
import com.example.tfg.models.Model


class profAdapter (private val profList: ArrayList<Model>) : RecyclerView.Adapter<profAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item,parent,false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: profAdapter.ViewHolder, position: Int) {
        val currentProf = profList[position]
        holder.tvProfName.text = currentProf.Nombre
        holder.tvProfApellido.text = currentProf.Apellido1
        holder.tvProfTitulo.text = currentProf.Titulo
        holder.tvProfGrado.text = currentProf.Grado
        holder.tvProfTlf.text = currentProf.Tlf
        holder.tvProfMail.text = currentProf.Email
    }


    override fun getItemCount(): Int {
        return profList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvProfName : TextView = itemView.findViewById(R.id.nombreProfesorRV)
        val tvProfApellido : TextView = itemView.findViewById(R.id.apellidoProfRV)
        val tvProfTitulo : TextView = itemView.findViewById(R.id.TituloProfRV)
        val tvProfGrado : TextView = itemView.findViewById(R.id.GradoProfRV)
        val tvProfTlf : TextView = itemView.findViewById(R.id.TlfProfRV)
        val tvProfMail : TextView = itemView.findViewById(R.id.MailProfRV)
    }

}