
import React from  'react';
import './ArtistApp.css'

class ArtistRow extends React.Component{

    handleDelete=(event)=>{
        console.log('delete button pentru '+this.props.artist.id);
        this.props.deleteFunc(this.props.artist.id);
    }

    render() {
        return (
            <tr>
                <td>{this.props.artist.id}</td>
                <td>{this.props.artist.firstName}</td>
                <td>{this.props.artist.lastName}</td>
                <td>{this.props.artist.age}</td>
                <td>{this.props.artist.originCountry}</td>
                <td><button  onClick={this.handleDelete}>Delete</button></td>
            </tr>
        );
    }
}

class ArtistTable extends React.Component {
    render() {
        let rows = [];
        let functieStergere=this.props.deleteFunc;
        this.props.artists.forEach(function(artist) {

            rows.push(<ArtistRow artist={artist}  key={artist.id} deleteFunc={functieStergere} />);
        });
        return (<div className="ArtistTable">

                <table className="center">
                    <thead>
                    <tr>
                        <th>Id</th>
                        <th>FirstName</th>
                        <th>LastName</th>
                        <th>Age</th>
                        <th>Origin Country</th>

                        <th>Actions</th>
                    </tr>
                    </thead>
                    <tbody>{rows}</tbody>
                </table>

            </div>
        );
    }
}

export default ArtistTable;