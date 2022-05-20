
import React from  'react';
import ArtistTable from './ArtistTable';
import './ArtistApp.css'
import ArtistForm from "./ArtistForm";
import {GetArtists, DeleteArtist, AddArtist, UpdateArtist} from './utils/rest-calls'


class ArtistApp extends React.Component{
    constructor(props){
        super(props);
        this.state={artists:[ {"id":55,"firstName":"Marina","lastName":"Raducu","age":35,"originCountry":"Romania"}],
            deleteFunc:this.deleteFunc.bind(this),
            addFunc:this.addFunc.bind(this),
            modifyFunc:this.modifyFunc.bind(this),
        }
        console.log('ArtistApp constructor')
        console.log(GetArtists());
    }

    addFunc(artist){
        console.log('inside add Func '+artist);
        AddArtist(artist)
            .then(res=> GetArtists())
            .then(artists=>this.setState({artists}))
            .catch(erorr=>console.log('eroare add ',erorr));
    }


    deleteFunc(artist){
        console.log('inside deleteFunc '+artist);
        DeleteArtist(artist)
            .then(res=> GetArtists())
            .then(artists=>this.setState({artists}))
            .catch(error=>console.log('eroare delete', error));
    }

    modifyFunc(id, artist){
        console.log('inside modifyFunc '+artist);
        UpdateArtist(id, artist)
            .then(res=> GetArtists())
            .then(artists=>this.setState({artists}))
            .catch(error=>console.log('eroare update', error));
    }


    componentDidMount(){
        console.log('inside componentDidMount')
        GetArtists().then(artists=>this.setState({artists}));
    }

    render(){
        return(
            <div className="ArtistApp">
                <h1>Festival Artist</h1>
                <ArtistForm addFunc={this.state.addFunc} modifyFunc={this.state.modifyFunc}/>
                <br/>
                <br/>
                <ArtistTable artists={this.state.artists} deleteFunc={this.state.deleteFunc}/>
            </div>
        );
    }
}

export default ArtistApp;